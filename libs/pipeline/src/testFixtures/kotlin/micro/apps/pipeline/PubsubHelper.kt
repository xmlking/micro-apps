package micro.apps.pipeline

import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import com.google.api.gax.core.CredentialsProvider
import com.google.api.gax.core.NoCredentialsProvider
import com.google.api.gax.grpc.GrpcTransportChannel
import com.google.api.gax.rpc.ApiException
import com.google.api.gax.rpc.FixedTransportChannelProvider
import com.google.api.gax.rpc.StatusCode.Code.NOT_FOUND
import com.google.api.gax.rpc.TransportChannelProvider
import com.google.cloud.pubsub.v1.Publisher
import com.google.cloud.pubsub.v1.SubscriptionAdminClient
import com.google.cloud.pubsub.v1.SubscriptionAdminSettings
import com.google.cloud.pubsub.v1.TopicAdminClient
import com.google.cloud.pubsub.v1.TopicAdminSettings
import com.google.common.util.concurrent.MoreExecutors
import com.google.pubsub.v1.ProjectSubscriptionName
import com.google.pubsub.v1.PubsubMessage
import com.google.pubsub.v1.PushConfig
import com.google.pubsub.v1.Subscription
import com.google.pubsub.v1.Topic
import com.google.pubsub.v1.TopicName
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import java.io.IOException
import java.util.concurrent.TimeUnit

class PubsubHelper(var host: String, var projectId: String) {
    var channel: ManagedChannel = ManagedChannelBuilder.forTarget(host).usePlaintext().build()
    var transportChannelProvider: TransportChannelProvider
    lateinit var topicAdminClient: TopicAdminClient
    lateinit var subscriptionAdminClient: SubscriptionAdminClient
    var credentialsProvider: CredentialsProvider = NoCredentialsProvider.create()

    @Throws(IOException::class)
    private fun createTopicAdmin(): TopicAdminClient {
        return TopicAdminClient.create(TopicAdminSettings.newBuilder().setTransportChannelProvider(transportChannelProvider)
            .setCredentialsProvider(credentialsProvider).build())
    }

    @Throws(IOException::class)
    private fun createSubscriberAdmin(): SubscriptionAdminClient {
        return SubscriptionAdminClient.create(SubscriptionAdminSettings.newBuilder().setTransportChannelProvider(transportChannelProvider)
            .setCredentialsProvider(credentialsProvider).build())
    }

    fun deleteTopic(topicName: String) {
        val projectTopicName = TopicName.of(projectId, topicName)
        topicAdminClient.deleteTopic(projectTopicName)
    }

    fun deleteSubscription(subscriptionName: String) {
        val projectSubscriptionName = ProjectSubscriptionName.of(projectId, subscriptionName)
        subscriptionAdminClient.deleteSubscription(projectSubscriptionName)
    }

    @Throws(ApiException::class)
    fun createTopic(topicName: String): Topic {
        val projectTopicName = TopicName.of(projectId, topicName)
        return topicAdminClient.createTopic(projectTopicName)
    }

    fun getTopic(topicName: String): Topic? {
        val projectTopicName = TopicName.of(projectId, topicName)
        var topic: Topic? = null
        try {
            topic = topicAdminClient.getTopic(projectTopicName)
        } catch (e: ApiException) {
            if (e.statusCode.code == NOT_FOUND) {
                println("topic not found")
            }
        } catch (e: Exception) {
            println(e.message)
        }
        return topic
    }

    fun hasTopic(topicName: String): Boolean {
        return null != getTopic(topicName)
    }

    fun createSubscription(topicName: String, subscriptionName: String): Subscription {
        val projectSubscriptionName = ProjectSubscriptionName.of(projectId, subscriptionName)
        val projectTopicName = TopicName.of(projectId, topicName)
        return subscriptionAdminClient.createSubscription(projectSubscriptionName, projectTopicName, PushConfig.getDefaultInstance(), 0)
    }

    fun sendMessage(topicName: String, message: PubsubMessage) {
        val projectTopicName = TopicName.of(projectId, topicName)
        var publisher: Publisher? = null
        try {
            publisher = Publisher.newBuilder(projectTopicName).setChannelProvider(transportChannelProvider).setCredentialsProvider(credentialsProvider).build()
            val future = publisher.publish(message)
            ApiFutures.addCallback(future, object : ApiFutureCallback<String?> {
                override fun onFailure(throwable: Throwable) {
                    throwable.printStackTrace()
                }

                override fun onSuccess(s: String?) {
                    // log.info("Sending Id =" + s);
                }
            }, MoreExecutors.directExecutor())
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (publisher != null) {
                publisher.shutdown()
                try {
                    publisher.awaitTermination(1, TimeUnit.MINUTES)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }

    init {
        transportChannelProvider = FixedTransportChannelProvider.create(GrpcTransportChannel.create(channel))
        try {
            topicAdminClient = createTopicAdmin()
            subscriptionAdminClient = createSubscriberAdmin()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
