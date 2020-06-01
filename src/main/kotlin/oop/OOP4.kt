package oop

import java.io.Serializable
import java.math.BigDecimal
import java.util.*

// Explicar propiedades en un constructor
class ToDoTask(var title: String, var description: String, var status: String = "CREATED")

fun checkInformationOnTask(task: ToDoTask) {
    println(task.title)
    println(task.description)
    println(task.status)
}


// Explicar parametros para un constructor vs propiedades en otro lado
open class Project(
    var title: String,
    val creator: Person,
    val members: List<Employee> = emptyList()
) {
    val created: Date = Date()

    private var status: String = "CREATED"
}


// Explicar inicializacion , cuando si y cuando no.
class ProjectOwner(
    name: String,
    lastName: String
) : Employee(name, lastName) {
//    private val roleName: String

//    var canAddSubProjects: Boolean = true
//    var currentCountry: String?
}


// Explicar inferencia de tipos
class Meeting(
    var title: String,
    var start: Date,
    var end: Date,
    val organizer: Employee
) {
    // El tipo no esta definido, la funcion mutableListOf no requiere el tipo
    val participants = mutableListOf<Employee>(organizer)

    // El compilador se adapta a lo que la funcion dicta que retorna
    val attachments = listOf("")

    // El compilador debe ser capaz de identificar los casos obvios
    var description = ""

    // El compilador puede determinar partiendo de lo obvio
    var webLink = MeetingLink("")

    class MeetingLink(
        val meetingName: String
    ) : Serializable {
        private val redirectsTo: String

        init {
            redirectsTo = obtainRedirect()
        }

        private fun obtainRedirect(): String = when {
            meetingName.contains("google") -> "meet.google.com/$meetingName"
            meetingName.contains("zoom.us") -> "${getRegion(meetingName)}.zoom.us/$meetingName"
            else -> "meet.jit.si/$meetingName"
        }
    }
}


// Explicar niveles de acceso de las propiedades
open class Consultant(
    name: String, // Depende de la clase padre
    lastName: String, // Depende de la clase padre
    val workDays: List<String>, // Public
    protected val company: Company,
    private var salary: BigDecimal
) : Employee(name, lastName)

// Demostrar que el nivel de acceso si esta limitado
class DevHackConsultant(
    name: String,
    lastName: String
) : Consultant(name, lastName, listOf("Monday", "Wednesday", "Friday"), devHackCompany, BigDecimal(10000000000L)) {

    val fullName = "$name $lastName"

    fun presentItself() {
        println("Hola, me llamo y trabajo para $company") // Accesible porque es protected
    }

    fun revealSecret() {
        println("La verdad es que gano… $salary") // Private en la clase padre? Inaccesible entonces
    }
}

// Demostrar que el nivel de acceso si esta limitado
class Course(val devHackMentor: DevHackConsultant) {
    fun presentMentor() {
        println(
            """
                Mentor: ${devHackMentor.fullName}
                Dias que se imparte el curso: ${devHackMentor.workDays.joinToString()}
                Compañia que imparte: ${devHackMentor.company}
                Se le paga al mentor: ${devHackMentor.salary}
            """.trimIndent()
        )
    }
}

// Explicar getter y setter
class CourseMaterial(
    val documents: List<String>,
    val notes: MutableList<String>,
    val author: Person
) {
    // Los valores se pueden iniciar partiendo de otras propiedades
    val lectures = documents.filter { it.endsWith("pdf") }

    // Podemos crear una propiedad con un custom get
    val documentsInThisMaterial: Int
        get() = documents.size

    // Y un custom set
    var latestReviewer: Person = author
        private set(value) {
            if (latestReviewer is Reviewer) {
                field = value
            }
        }

    // PERO cuando tenemos un custom get, recordemos que val != valor inmutable
    val notesCount: Int
        get() = notes.size
}

// Abstract properties
abstract class CoursePresentation(val courseMaterial: CourseMaterial) {
    abstract val title: String
}

class KotlinPresentation(courseMaterial: CourseMaterial) : CoursePresentation(courseMaterial) {
    // Necesario sobre escribirla!
    override val title: String
        get() = "Kotlin Workshop"
}

// Interface properties
interface CourseMentor {
    val fullName: String

    val materials: CourseMaterial

    fun explainMaterials()
}

class DevHackMentor(firstName: String, lastName: String) : CourseMentor {
    // Como estan definidas en la interfaz, debemos sobre escribirla
    override val fullName: String = "$firstName $lastName"

    override val materials = CourseMaterial(emptyList(), mutableListOf(), Author("Juan Guillermo", "Gomez"))

    override fun explainMaterials() {

    }
}

// Companion object properties
class PointOfContact(name: String, lastName: String) : Person(name, lastName) {
    override fun getRole(): String = "pointofcontact"

    companion object Fundador : Person("Juan Guillermo", "Gomez") {
        override fun getRole(): String = "fundador"

        fun askForHelp(): PointOfContact {
            println("Buscando a quien contactar…")

            return PointOfContact("Sinuhe", "Jaime")
        }
    }
}

fun callPointOfContact() {
    // Tener una implementacion especifica de alguna clase
    println(PointOfContact.Fundador)
    // Sirve para dar acceso a funciones desde la clase, no desde instancias
    println(PointOfContact.askForHelp())
}


// Delegacion de interfaces mediante una propiedad
class ToDoList(private val tasks: MutableList<ToDoTask>) : MutableList<ToDoTask> by tasks

// Delegacion de propiedades mediante lazy

// Delegacion de propiedades mediante observable

// Delegacion de propiedades mediante un mapa

// Delegacion de propiedades mediante Vetoable


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