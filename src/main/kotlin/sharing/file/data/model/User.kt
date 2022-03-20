package sharing.file.data.model

class User(
    val name: String,
    val privateKey: ByteArray,
    val publicKey: ByteArray
)