package sharing.file.data.manager

import java.security.KeyFactory
import java.security.MessageDigest
import java.security.Signature
import java.security.spec.X509EncodedKeySpec

object CryptoManager {
    // SHA1 - Алгоритм хеширования документа
    // RSA - Алгоритм подписи документа
    // SHA1 - Алгоритм хеширования открытого ключа
    // RSA - Алгоритм подписи открытого ключа

    fun sha1(text: String): ByteArray {
        return MessageDigest.getInstance("SHA-1").digest(text.toByteArray())
    }

    fun createSignature(
        data: ByteArray,
        privateKeyEncoded: ByteArray
    ): ByteArray {
        val signature = Signature.getInstance("SHA1withRSA")
        val factory = KeyFactory.getInstance("RSA")
        val privateKey = factory.generatePrivate(X509EncodedKeySpec(privateKeyEncoded))
        signature.initSign(privateKey)
        signature.update(data)
        return signature.sign()
    }

    fun sign(
        publicKeyEncoded: ByteArray,
        data: ByteArray,
        digitalSignature: ByteArray
    ): Boolean {
        val signature = Signature.getInstance("SHA1withRSA")

        val ks = X509EncodedKeySpec(publicKeyEncoded)
        val publicKey = KeyFactory.getInstance("RSA")
            .generatePublic(ks)
        signature.initVerify(publicKey)
        signature.update(data)
        return signature.verify(digitalSignature)
    }
}