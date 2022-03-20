package sharing.file.data.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*

class OpenKey(
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
    val blob: ByteArray
) {
    suspend fun write(path: String) {
        withContext(Dispatchers.IO) {
            val file = File(path)
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            DataOutputStream(FileOutputStream(path)).run {
                writeInt(nameLen)
                writeInt(blobLen)
                write(name)
                write(blob)
                close()
            }
        }
    }

    companion object {
        suspend fun read(path: String): OpenKey {
            return withContext(Dispatchers.IO) {
                DataInputStream(FileInputStream(path)).run {
                    val nameLen = readInt()
                    val blobLen = readInt()
                    OpenKey(
                        nameLen,
                        blobLen,
                        name = readNBytes(nameLen),
                        blob = readNBytes(blobLen)
                    )
                }
            }
        }

        const val type = "pub"
    }
}