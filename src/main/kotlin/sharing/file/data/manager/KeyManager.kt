package sharing.file.data.manager

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import sharing.file.data.model.User
import java.io.*
import java.security.KeyPairGenerator

object KeyManager {
    private val mutex = Mutex()

    suspend fun getUser(name: String) = mutex.withLock { KeyStore.getUser(name) }

    suspend fun getPrivateKey(name: String) {
    }

    suspend fun getPublicKey(name: String) {
    }

    suspend fun deletePair(name: String) = mutex.withLock { KeyStore.deleteUser(name) }


    object KeyStore {
        // name_length, private_length, public_length, name, private_key, public_key
        private const val path = "keystore"

        suspend fun getUser(name: String): User {
            val input = load()
            val nameLen = name.toByteArray().size
            val privateKey: ByteArray
            val publicKey: ByteArray
            while (input.available() > 0) {
                val currentNameLen = input.readInt()
                val privateLen = input.readInt()
                val publicLen = input.readInt()
                if (nameLen == currentNameLen) {
                    if (name == String(input.readNBytes(nameLen))) {
                        privateKey = input.readNBytes(privateLen)
                        publicKey = input.readNBytes(publicLen)
                        return User(name, privateKey, publicKey)
                    }
                    input.skipNBytes((privateLen + publicLen).toLong())
                } else {
                    input.skipNBytes((currentNameLen + privateLen + publicLen).toLong())
                }
            }
            input.close()
            val pair = KeyPairGenerator.getInstance("RSA").generateKeyPair()
            privateKey = pair.private.encoded
            publicKey = pair.public.encoded
            val out = open()
            out.run {
                writeInt(nameLen)
                writeInt(privateKey.size)
                writeInt(publicKey.size)
                write(name.toByteArray())
                write(privateKey)
                write(publicKey)
                close()
            }
            return User(name, privateKey, publicKey)
        }

        suspend fun deleteUser(name: String): Boolean {
            val input = load()
            val out = open(false)
            val nameLen = name.toByteArray().size
            var find = false
            while (input.available() > 0) {
                val currentNameLen = input.readInt()
                val privateLen = input.readInt()
                val publicLen = input.readInt()
                if (nameLen == currentNameLen) {
                    val nameBytes = input.readNBytes(nameLen)
                    val currentName = String(nameBytes)
                    if (name != currentName) {
                        out.writeInt(currentNameLen)
                        out.writeInt(privateLen)
                        out.writeInt(publicLen)
                        out.write(nameBytes)
                        out.write(input.readNBytes(privateLen + publicLen))
                    } else {
                        // удаляемая запись
                        input.skipNBytes((privateLen + publicLen).toLong())
                        out.write(input.readNBytes(input.available()))
                        find = true
                    }
                } else {
                    out.writeInt(currentNameLen)
                    out.writeInt(privateLen)
                    out.writeInt(publicLen)
                    out.write(input.readNBytes(currentNameLen + privateLen + publicLen))
                }
            }
            input.close()
            out.close()
            return find
        }

        private fun load(): DataInputStream {
            if (!File(path).exists()) File(path).createNewFile()
            return DataInputStream(ByteArrayInputStream(FileInputStream(path).readNBytes(Int.MAX_VALUE)))
        }

        private fun open(append: Boolean = true): DataOutputStream {
            return DataOutputStream(FileOutputStream(path, append))
        }
    }
}