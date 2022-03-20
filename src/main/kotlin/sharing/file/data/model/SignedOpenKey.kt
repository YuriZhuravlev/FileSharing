package sharing.file.data.model

class SignedOpenKey(
    /**
     * Длина имени владельца открытого ключа
     */
    val nameLen: Int,
    /**
     * Длина блоба с открытым ключом
     */
    val blobLen: Int,
    /**
     * Имя владельца открытого ключа
     */
    val name: ByteArray,
    /**
     * Блоб с открытым ключом
     */
    val blob: ByteArray,
    /**
     * Электронная подпись
     */
    val sign: ByteArray
) {
    companion object {
        const val type = "sk"
    }
}