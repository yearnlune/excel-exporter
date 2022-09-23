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

/**
 * Support 'Amazon S3' to excel exporter
 *
 * @property s3Client amazon S3 interface
 * @property bucket bucket name
 * @property expiration time (in seconds) that the new pre-signed URL expires, default 600 seconds(10 min)
 */
class S3Supporter(

    private val s3Client: AmazonS3,

    private val bucket: String,

    private var expiration: Long = 600L
) {

    /**
     * Set pre-signed expiration
     *
     * @param second
     */
    fun withExpiration(second: Long) {
        if (second < 0L) {
            throw IllegalArgumentException("Negative number not supported [Expiration: $second]")
        }

        this.expiration = second
    }

    /**
     * Upload the Excel and then return download url
     *
     * @param key s3 object key
     * @param workbook excel
     * @return download url
     */
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

    /**
     * Upload excel to S3
     *
     * @param key object key
     * @param workbook excel
     * @return result
     */
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

    /**
     * Creates pre-signed url
     *
     * @param key object key
     * @return download url
     */
    private fun getPresignedDownloadUrl(key: String): String {
        val presignedRequest = GeneratePresignedUrlRequest(bucket, key).withMethod(HttpMethod.GET)
            .withContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")

        if (expiration > 0L) {
            presignedRequest.withExpiration(Date(System.currentTimeMillis() + (expiration * 1000)))
        }

        return s3Client.generatePresignedUrl(presignedRequest).toString()
    }
}