package io.github.yearnlune.excel

import com.amazonaws.AmazonServiceException
import com.amazonaws.HttpMethod
import com.amazonaws.SdkClientException
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectResult
import org.apache.poi.ss.usermodel.Workbook
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*


class S3Supporter(

    private val s3Client: AmazonS3,

    private val bucket: String,

    private var expiration: Long = 0L
) {

    init {
        if (!s3Client.doesBucketExistV2(bucket)) {
            s3Client.createBucket(bucket)
        }
    }

    fun withExpiration(second: Long) {
        this.expiration = second
    }

    fun uploadAndGetDownloadUrl(key: String, workbook: Workbook): String? {
        val generatedKey = "excel/$key.xlsx"
        var downloadUrl: String? = null

        try {
            putExcelObject(generatedKey, workbook)
            downloadUrl = getPresignedDownloadUrl(generatedKey)
        } catch (e: SdkClientException) {
            e.printStackTrace()
        } catch (e: AmazonServiceException) {
            e.printStackTrace()
        }

        return downloadUrl
    }

    @kotlin.jvm.Throws(SdkClientException::class, AmazonServiceException::class)
    private fun putExcelObject(key: String, workbook: Workbook): PutObjectResult? {
        val outputStream = ByteArrayOutputStream()
        workbook.write(outputStream)
        val dataStream = outputStream.toByteArray()
        val inputStream = ByteArrayInputStream(dataStream)

        return s3Client.putObject(bucket, key, inputStream, ObjectMetadata().apply {
            contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            contentLength = dataStream.size.toLong()
        })
    }

    private fun getPresignedDownloadUrl(key: String): String {
        val presignedRequest = GeneratePresignedUrlRequest(bucket, key).withMethod(HttpMethod.GET)
            .withContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            .withExpiration(Date(System.currentTimeMillis() + (expiration * 1000)))

        return s3Client.generatePresignedUrl(presignedRequest).toString()
    }
}