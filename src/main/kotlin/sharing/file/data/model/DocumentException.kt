package sharing.file.data.model

open class DocumentException(message: String) : Throwable(message)

/** Документ с заданным именем не найден */
class DocumentNotFound(path: String) : DocumentException("Не удалось найти файл по заданному пути $path")

class DocumentFailed(message: String = "Не удалось декодировать файл") : DocumentException(message)

/** Отсутствует открытый ключ автора документа */
class OpenKeyNotFound(message: String = "Отсутствует открытый ключ автора документа") : DocumentException(message)

/** Подпись открытого ключа не подтверждена */
class OpenKeyFailedSigned(message: String = "Подпись открытого ключа не подтверждена") : DocumentException(message)
