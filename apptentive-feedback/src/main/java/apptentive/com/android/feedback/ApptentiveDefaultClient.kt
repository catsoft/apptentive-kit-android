package apptentive.com.android.feedback

import android.content.Context
import androidx.annotation.WorkerThread
import apptentive.com.android.feedback.backend.ConversationService
import apptentive.com.android.feedback.backend.DefaultConversationService
import apptentive.com.android.feedback.conversation.ConversationManager
import apptentive.com.android.feedback.conversation.DefaultConversationRepository
import apptentive.com.android.feedback.conversation.DefaultConversationSerializer
import apptentive.com.android.feedback.engagement.Event
import apptentive.com.android.feedback.platform.*
import apptentive.com.android.network.HttpClient
import apptentive.com.android.util.FileUtil
import java.io.File

internal class ApptentiveDefaultClient(
    private val apptentiveKey: String,
    private val apptentiveSignature: String,
    private val httpClient: HttpClient
) : ApptentiveClient {
    private lateinit var conversationService: ConversationService
    private lateinit var conversationManager: ConversationManager

    @WorkerThread
    internal fun start(context: Context) {
        conversationService = DefaultConversationService(
            httpClient = httpClient,
            apptentiveKey = apptentiveKey,
            apptentiveSignature = apptentiveSignature,
            apiVersion = Constants.API_VERSION,
            sdkVersion = Constants.SDK_VERSION,
            baseURL = Constants.SERVER_URL
        )

        val conversationFile = getConversationFile(context) // FIXME: remove android specific api
        val manifestFile = getManifestFile(context)
        conversationManager = ConversationManager(
            conversationRepository = DefaultConversationRepository(
                conversationSerializer = DefaultConversationSerializer(
                    conversationFile = conversationFile,
                    manifestFile = manifestFile
                ),
                appReleaseFactory = DefaultAppReleaseFactory(context),
                personFactory = DefaultPersonFactory(),
                deviceFactory = DefaultDeviceFactory(context),
                sdkFactory = DefaultSDKFactory(
                    version = Constants.SDK_VERSION,
                    distribution = "Default",
                    distributionVersion = Constants.SDK_VERSION
                ),
                manifestFactory = DefaultEngagementManifestFactory()
            ),
            conversationService = conversationService
        )
    }

    override fun engage(context: Context, event: Event): EngagementResult {
        TODO()
    }

    companion object {
        fun getConversationFile(context: Context): File {
            val conversationsDir = getConversationDir(context)
            return File(conversationsDir, "conversation.bin")
        }

        fun getManifestFile(context: Context): File {
            val conversationsDir = getConversationDir(context)
            return File(conversationsDir, "manifest.bin")
        }

        private fun getConversationDir(context: Context): File {
            return FileUtil.getInternalDir(
                context = context,
                path = "conversations",
                createIfNecessary = true
            )
        }
    }
}