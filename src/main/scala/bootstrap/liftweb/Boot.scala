package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._

import common._
import http._
import sitemap._
import Loc._
import mapper._

import org.unsane.spirit.planningweb.model._

import de.codecarving.fhsldap.fhsldap
import de.codecarving.fhsldap.model.User
import de.codecarving.fhsldap.model.LDAPUtils

import mongodb._


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {

    // to selcet Persistence
    val usedPersistence = Props.get("spirit.pers.layer") openOr ""

    usedPersistence match {
      case "mongoDB" =>
        //connecting to MongoDB with user and pass
        MongoDB.defineDbAuth(DefaultMongoIdentifier,
          MongoAddress(MongoHost("127.0.0.1", 27017), "spirit_curriculum"),
          "spirit_curriculum",
          "spirit_curriculum")
      case _ => error("Could not support: " + usedPersistence) // noch mal schauen!!!
    }

     // we need this for ressources
     ResourceServer.allow {
      case "css" :: _ => true
     }

    // to check if the current User is registered in Configfile
    def is(user: String): Boolean = {
      Props.get(user, "")
        .split(";")
        .contains(User.currentUserId.openOr(""))
    }

    // to check if the current User is a SuperUser
    def isSuperUser : Boolean = {
      is("spirit.server.admins")
    }

    // to check if the current User is a CourseSheduler
    def isCourseSheduler : Boolean = {
      is("spirit.course.user")
    }

    // to check if the current User is a TimetableSheduler
    def isTimetableSheduler : Boolean = {
      is("spirit.timetable.user")
    }

    // to create a Admin-Area
    val ifSuperUser = If (() => isSuperUser,
                          () => RedirectResponse("/index"))

    // to create a Dozent-Area
    val ifDozent = If (() => if (isSuperUser || User.isEmployee) {true}
                             else {false},
                       () => RedirectResponse("/index"))

    // to create a CourseSheduler-Area
    val ifCourseSheduler = If (() => if (isCourseSheduler || isSuperUser) {true}
                                     else {false},
                               () => RedirectResponse("/index"))

    // to create a TimetableSheduler-Area
    val ifTimetableSheduler = If (() => if (isTimetableSheduler || isSuperUser) {true}
                                        else {false},
                                  () => RedirectResponse("/index"))

    // Build SiteMap
    def sitemap = SiteMap( Menu.i("Home") / "index" >> User.AddUserMenusAfter,
                             Menu(Loc ("Wunschzeiten", List("wishtimes"),"Wunschzeiten", ifDozent)),
                           // the menue for dozentmanagement
                           Menu.i("Dozentenverwaltung") / "dozent" / "management" >> ifCourseSheduler submenus(
                             Menu.i("Typ") / "dozent" / "type" >> LocGroup("top_dozent"),
                             Menu.i("Dozent") / "dozent" / "dozent" >> LocGroup("top_dozent"),
                             Menu.i("FHS-Dozent") / "dozent" / "fhsdozent" >> LocGroup("top_dozent")
                           ),
                           // the menue for coursemanagement
                           Menu.i("Studiengangverwaltung") / "course" / "management" >> ifCourseSheduler submenus(
                             Menu.i("Studiengang") / "course" / "course" >> LocGroup("top_course")
                           ),
                           // the menue for lecturemanagement
                           Menu.i("LV-Management") / "lecture" / "management" >> ifCourseSheduler submenus(
                             Menu.i("Art") / "lecture" / "type" >> LocGroup("top_lecture"),
                             Menu.i("LV erfassen") / "lecture" / "create" >> LocGroup("top_lecture"),
                             Menu.i("LV anzeigen") / "lecture" / "show" >> LocGroup("top_lecture"),
                             Menu.i("LV ändern") / "lecture" / "update" >> LocGroup("top_lecture"),
                             Menu.i("LV löschen") / "lecture" / "delete" >> LocGroup("top_lecture"),
                             Menu.i("LV SS/WS zuweisen") / "lecture" / "selectsemester" >> LocGroup("top_lecture"),
                             Menu.i("VL Gruppen erstellen") / "lecture" / "lecturerelation" >> LocGroup("top_lecture"),
                             Menu.i("Übungsgruppe erstellen") / "lecture" / "tutorialrelation" >> LocGroup("top_lecture"),
                             Menu.i("VL-/Übungsgruppe löschen") / "lecture" / "relationdelete" >> LocGroup("top_lecture"),
                             Menu.i("Semesterplanung") / "lecture" / "commitplan" >> LocGroup("top_lecture")
                           ),
                           // the menue for roommanagement
                           Menu.i("Raumverwaltung") / "room" / "management" >> ifTimetableSheduler submenus(
                             Menu.i("Raum") / "room" / "room" >> LocGroup("top_room")
                           ),
                           // the menue for worktimemanagement
                           Menu.i("Arbeitszeitmanagement") / "worktime" / "management" >> ifTimetableSheduler submenus(
                             Menu.i("Einstellungen") / "worktime" / "setting" >> LocGroup("top_worktime"),
                             Menu.i("Arbeitszeiten") / "worktime" / "show" >> LocGroup("top_worktime")
                           )
                         )


    // Registring the snippet packages.
    LiftRules.addToPackages("org.unsane.spirit.planningweb")
    LiftRules.addToPackages("org.unsane.spirit.planningweb.snippet.dozents")
    LiftRules.addToPackages("org.unsane.spirit.planningweb.snippet.courses")
    LiftRules.addToPackages("org.unsane.spirit.planningweb.snippet.rooms")
    LiftRules.addToPackages("org.unsane.spirit.planningweb.snippet.worktimes")
    LiftRules.addToPackages("org.unsane.spirit.planningweb.snippet.lectures")

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out
   LiftRules.setSiteMap(sitemap)

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
    
    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // What is the function to test if a user is logged in?
    LiftRules.loggedInTest = Full(() => User.loggedIn_?)

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))    


    // This is where we startup our fhsldap user stuff!
    fhsldap.init

  }
}
