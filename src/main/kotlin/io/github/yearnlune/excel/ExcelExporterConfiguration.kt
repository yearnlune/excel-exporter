package io.github.yearnlune.excel

import com.amazonaws.services.s3.AmazonS3

/**
 * The ExcelExporterConfiguration offer various configurations
 *
 */
class ExcelExporterConfiguration {

    var s3Supporter: S3Supporter? = null
        private set

    /**
     * Configures the S3 to be used
     *
     * @param s3Client amazon S3 interface
     * @param bucketName bucket name
     * @return S3Supporter
     */
    fun usingS3(s3Client: AmazonS3, bucketName: String): S3Supporter {
        this.s3Supporter = S3Supporter(s3Client, bucketName)
        return this.s3Supporter!!
    }
}