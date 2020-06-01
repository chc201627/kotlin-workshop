package oop

class Reviewer(name: String, lastName: String) : Person(name, lastName) {
    override fun getRole() = "Reviewer"
}

class Author(name: String, lastName: String) : Person(name, lastName) {
    override fun getRole() = "Author"
}

fun getRegion(url: String): String = "us"

val devHackCompany = Company(
    "DevHack",
    "5555555",
    emptyList(),
    listOf(Manager("Juan guillermo", "Gomez", 21))
)

fun checkWhoseStillPayingAttention(): List<Person> = emptyList()

fun containsBadWords(question: String) = true