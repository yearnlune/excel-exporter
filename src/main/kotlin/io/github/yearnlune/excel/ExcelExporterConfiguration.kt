package io.github.yearnlune.excel

import com.amazonaws.services.s3.AmazonS3

class ExcelExporterConfiguration {

    var s3Supporter: S3Supporter? = null

    fun usingS3(s3Client: AmazonS3, bucketName: String): S3Supporter {
        this.s3Supporter = S3Supporter(s3Client, bucketName)
        return this.s3Supporter!!
    }
}