// This code is based on (10.01.2015):
// https://github.com/greencatsoft/scalajs-angular-todomvc/blob/master/scalajs/src/main/scala/todomvc/example/TodoCtrl.scala
package todomvc.example

import biz.enef.angular.core.Location
import biz.enef.angular.{Scope, Controller}

import scala.scalajs.js
import js.Dynamic.literal

class TodoCtrl($location: Location, $scope: Scope) extends Controller {
  val $dynamicScope = $scope.asInstanceOf[js.Dynamic]
  var todos = js.Array[Task]()
  var incrementer = 0;
  var newTitle = ""
  var allChecked = true
  var remainingCount = 0
  var statusFilter = literal()
  def path() = $location.path()

  $scope.$watch(() => $location.path(), (path: String) =>
    statusFilter = path match {
      case "/active" => literal(completed = false)
      case "/completed" => literal(completed = true)
      case _ => literal()
    }
  )

  def save(todo: Task): Unit = update()

  def add(): Unit = {
    val title = newTitle.trim
    incrementer = incrementer + 1
    if(title != "") add(Task(title, false, incrementer))
  }

  private def add(todo: Task): Unit = {
      todos :+= todo
      newTitle = ""
      update()
  }

  def remove(todo: Task): Unit = {
    todos = todos.filter( _.id != todo.id )
    update()
  }

  def clearCompleted(): Unit = {
    todos = todos.filter( !_.completed )
    update()
  }

  def markAll(completed: Boolean): Unit = {
    todos.foreach( _.completed = completed)
    update()
  }

  private def update(): Unit = {
    inConsole(todos)
    remainingCount = todos.count(! _.completed)
    allChecked = remainingCount == 0
  }

  private def inConsole(obj: js.Any): Unit = js.Dynamic.global.console.info(obj)

}
