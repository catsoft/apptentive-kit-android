package apptentive.com.android.concurrent

/**
 * Concrete implementation of the [Promise] interface
 *
 * @param [completionQueue] optional execution queue to invoke async fulfilment/rejection callbacks.
 */
class PromiseImpl<T>(private val completionQueue: ExecutionQueue? = null) : Promise<T> {
    private var valueCallback: (value: T) -> Unit = {}
    private var errorCallback: (e: Exception) -> Unit = {}

    override fun then(onValue: (value: T) -> Unit): Promise<T> {
        valueCallback = onValue
        return this
    }

    override fun catch(onError: (e: Exception) -> Unit): Promise<T> {
        errorCallback = onError
        return this
    }

    fun onValue(value: T) {
        if (completionQueue != null) {
            completionQueue.dispatch {
                onValueSync(value)
            }
        } else {
            onValueSync(value)
        }
    }

    fun onError(e: Exception) {
        if (completionQueue != null) {
            completionQueue.dispatch {
                onErrorSync(e)
            }
        } else {
            onErrorSync(e)
        }
    }

    private fun onValueSync(value: T) {
        try {
            valueCallback(value)
        } catch (e: Exception) {
            errorCallback(e)
        }
    }

    private fun onErrorSync(e: Exception) {
        errorCallback(e)
    }
}