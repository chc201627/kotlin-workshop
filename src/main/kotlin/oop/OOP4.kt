package oop

import java.io.Serializable
import java.math.BigDecimal
import java.util.*
import java.util.function.UnaryOperator
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

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
//        println("La verdad es que gano… $salary") // Private en la clase padre? Inaccesible entonces
    }
}

// Demostrar que el nivel de acceso si esta limitado
class Course(val devHackMentor: DevHackConsultant) {
    fun presentMentor() {
        println(
            """
                Mentor: ${devHackMentor.fullName}
                Dias que se imparte el curso: ${devHackMentor.workDays.joinToString()}
                Compañia que imparte: ${devHackMentor/*.company*/}
                Se le paga al mentor: ${devHackMentor/*.salary*/}
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
class BadToDoList(private val tasks: MutableList<ToDoTask>) : MutableList<ToDoTask> {
    override val size: Int
        get() = tasks.size

    override fun contains(element: ToDoTask): Boolean = tasks.contains(element)

    override fun containsAll(elements: Collection<ToDoTask>): Boolean = tasks.containsAll(elements)

    override fun get(index: Int): ToDoTask = tasks[index]

    override fun indexOf(element: ToDoTask): Int = tasks.indexOf(element)

    override fun isEmpty(): Boolean = tasks.isEmpty()

    override fun iterator(): MutableIterator<ToDoTask> = tasks.iterator()

    override fun lastIndexOf(element: ToDoTask): Int = tasks.lastIndexOf(element)

    override fun add(element: ToDoTask): Boolean = tasks.add(element)

    override fun add(index: Int, element: ToDoTask) {
        tasks.add(index, element)
    }

    override fun addAll(index: Int, elements: Collection<ToDoTask>): Boolean = tasks.addAll(index, elements)

    override fun addAll(elements: Collection<ToDoTask>): Boolean = tasks.addAll(elements)

    override fun clear() {
        tasks.clear()
    }

    override fun listIterator(): MutableListIterator<ToDoTask> = tasks.listIterator()

    override fun listIterator(index: Int): MutableListIterator<ToDoTask> = tasks.listIterator(index)

    override fun remove(element: ToDoTask): Boolean = tasks.remove(element)

    override fun removeAll(elements: Collection<ToDoTask>): Boolean = tasks.removeAll(elements)

    override fun removeAt(index: Int): ToDoTask = tasks.removeAt(index)

    override fun replaceAll(operator: UnaryOperator<ToDoTask>) = tasks.replaceAll(operator)

    override fun retainAll(elements: Collection<ToDoTask>): Boolean = tasks.retainAll(elements)

    override fun set(index: Int, element: ToDoTask): ToDoTask = tasks.set(index, element)

    override fun sort(c: Comparator<in ToDoTask>) {
        tasks.sortWith(c)
    }

    override fun spliterator(): Spliterator<ToDoTask> = tasks.spliterator()

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<ToDoTask> = tasks.subList(fromIndex, toIndex)
}


class ToDoList(private val tasks: MutableList<ToDoTask>) : MutableList<ToDoTask> by tasks

// Delegacion de propiedades mediante lazy
class CourseAttendants {
    val stillAwake: List<Person> by lazy { checkWhoseStillPayingAttention() }
}

// Delegacion de propiedades mediante observable
class CourseInCallAttendants {
    val stillPayingAttention: List<Person> by Delegates.observable(emptyList()) { _prop, oldV, newV ->
        println("La gente que antes estaba poniendo atencion eran: ${oldV.joinToString()}")
        println("Ahora estan poniendo atencion: ${newV.joinToString()}")
    }
}

// Delegacion de propiedades mediante Vetoable
class CourseQuestion {
    val questionToAnswer: String by Delegates.vetoable("No question") { _property, oldQuestion, newQuestion ->
        println("Estabamos respondiendo… $oldQuestion")

        val shouldWeAnswer = newQuestion.isNotBlank() || !containsBadWords(newQuestion)

        println("Debemos responder la nueva pregunta? ${if (shouldWeAnswer) "Si" else "No"}")

        shouldWeAnswer
    }
}

// Delegacion de propiedades mediante un mapa
class CourseContactInformation(mentorsData: Map<String, Person>) {
    val principalContact: PointOfContact by mentorsData

    val otherMentor by mentorsData
}

// Delegacion manual
class CourseSessionDelegate {
    private var count = 1
    private var sessionName = "Introduccion"

    operator fun getValue(thisRef: Any, property: KProperty<*>): String {
        return "Esta es la sesion numero: $count, y trata sobre: $sessionName"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("Actulizando el nombre de la sesion a: $value")
        count++
        sessionName = value
    }
}

class CourseSession {
    var session by CourseSessionDelegate()
}

fun runSessions(){
    val courseSession = CourseSession()
    println(courseSession.session)

    courseSession.session = "OOP en Kotlin"
    println(courseSession.session)

    courseSession.session = "Properties y otras brujerias"
    println(courseSession.session)
}