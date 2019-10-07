package apptentive.com.android.feedback.model

data class Conversation(
    val localIdentifier: String,
    val conversationToken: String? = null,
    val conversationId: String? = null,
    val device: Device,
    val person: Person,
    val sdk: SDK,
    val appRelease: AppRelease,
    val engagementManifest: EngagementManifest
)

val Conversation.hasConversationToken get() = this.conversationToken != null