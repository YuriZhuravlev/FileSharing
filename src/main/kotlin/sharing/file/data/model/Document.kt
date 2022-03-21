package sharing.file.data.model

class Document(
    /**
     * Длина имени подписывающего пользователя
     */
    val nameLen: Int,
    /**
     * Длина подписи
     */
    val signLen: Int,
    /**
     * Имя подписывающего пользователя
     */
    val name: String,
    /**
     * Электронная подпись
     */
    val sign: ByteArray,
    /**
     * Текст документа
     */
    val text: String
) {
    var verify: Verify = Verify.Empty

    enum class Verify {
        NotFoundOpenKey, FailedSignedOpenKey, FailedSigned, Success, Empty
    }

    companion object {
        const val type = "sd"
    }
}