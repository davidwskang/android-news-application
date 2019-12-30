package com.davidwskang.quicknewsapp.common

class Constants() {
    companion object {
        const val SEARCH_INDEX      = 0
        const val HEADLINES_INDEX   = 1
        const val BOOKMARKS_INDEX   = 2

        const val SEARCH_TITLE      = "Search"
        const val HEADLINES_TITLE   = "Top Headlines"
        const val BOOKMARKS_TITLE   = "Bookmarks"

        const val SAVED_DB      = "saved-database"
        const val SEARCH_DB     = "search-database"

        const val EMPTY_STRING = ""

        const val LOCATION_BASE_URL = "http://ip-api.com/"
        const val NEWS_API_BASE_URL = "https://newsapi.org/v2/"

        @JvmField
        val COUNTRY_CODES = arrayOf(
                "ae", "ar", "at", "au", "be", "bg", "br", "ca",
                "ch", "cn", "co", "cu", "cz", "de", "eg", "fr",
                "gb", "gr", "hk", "hu", "id", "ie", "il", "in",
                "it", "jp", "kr", "lt", "lv", "ma", "mx", "my",
                "ng", "nl", "no", "nz", "ph", "pl", "pt", "ro",
                "rs", "ru", "sa", "se", "sg", "si", "sk", "th",
                "tr", "tw", "ua", "us", "ve", "za" ) // len = 54

        @JvmField
        val COUNTRY_NAMES = arrayOf(
                "UAE","Argentina","Austria","Australia","Belgium","Bulgaria","Brazil","Canada",
                "Switzerland","China","Colombia","Cuba","Czech Republic","Germany","Egypt", "France",
                "United Kingdom","Greece","Hong Kong","Hungary","Indonesia","Ireland", "Israel","India",
                "Italy","Japan","South Korea","Lithuania","Latvia","Morocco", "Mexico","Malaysia",
                "Nigeria","Netherlands","Norway","New Zealand","Philippines", "Poland","Portugal","Romania",
                "Serbia","Russia","Saudi Arabia","Sweden","Singapore", "Slovenia","Slovakia","Thailand",
                "Turkey","Taiwan","Ukraine","United States", "Venezuela","South Africa")

        @JvmField
        val CATEGORIES = arrayOf("General", "Business", "Entertainment", "Health",
                "Science", "Sports", "Technology")
    }
}