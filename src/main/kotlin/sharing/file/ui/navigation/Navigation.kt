package sharing.file.ui.navigation

enum class Navigation {
    /**
     * Начальное состояние (пользователь не авторизован)
     */
    Splash,

    /**
     * О программе
     */
    About,

    /**
     * Ввод имени пользователя
     */
    Login,

    /**
     * Основное меню
     */
    Main,

    /**
     * Изменение документа
     */
    EditDocument
}