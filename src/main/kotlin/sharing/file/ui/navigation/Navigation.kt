package sharing.file.ui.navigation

enum class Navigation {
    /**
     * Начальное состояние (пользователь не авторизован)
     */
    Login,

    /**
     * О программе
     */
    About,

    /**
     * Ввод имени пользователя
     */
    SelectUser,

    /**
     * Основное меню
     */
    Main,

    /**
     * Изменение документа
     */
    EditDocument,

    /**
     * Чтение документа
     */
    ViewDocument
}