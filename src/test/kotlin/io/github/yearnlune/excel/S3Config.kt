package io.github.yearnlune.excel

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.AnonymousAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import io.findify.s3mock.S3Mock
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
@Profile("test")
open class S3Config(

    private val appProperties: AppProperties
) {

    private val api: S3Mock = S3Mock.Builder().withPort(appProperties.port).build()

    init {
        api.start()
    }

    @Bean
    @Qualifier("s3Client")
    open fun s3Client(): AmazonS3 {
        return AmazonS3ClientBuilder
            .standard()
            .withPathStyleAccessEnabled(true)
            .withEndpointConfiguration(s3Endpoint())
            .withCredentials(AWSStaticCredentialsProvider(AnonymousAWSCredentials()))
            .build()
    }

    private fun s3Endpoint(): EndpointConfiguration {
        return EndpointConfiguration("http://${appProperties.host}:${appProperties.port}", "us-west-1")
    }
}