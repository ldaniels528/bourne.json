package com.github.ldaniels528.bourne
package client

import com.github.ldaniels528.bourne.client.controllers._
import com.github.ldaniels528.bourne.client.services.{JobService, WebSocketService, WorkflowService}
import io.scalajs.npm.angularjs.uirouter.{RouteProvider, RouteTo}
import io.scalajs.npm.angularjs.{Module, Scope, Timeout, angular}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

/**
  * Bourne Client JavaScript Application
  * @author lawrence.daniels@gmail.com
  */
object BourneClientJSApp extends js.JSApp {

  @JSExport
  override def main(): Unit = {
    // create the application
    val module = angular.createModule("bourne",
      js.Array("ngAnimate", "ngCookies", "ngRoute", "ngSanitize", "nvd3", "angularFileUpload", "toaster", "ui.bootstrap"))

    // add the custom filters
    module.filter("duration", Filters.duration)
    module.filter("bytes", Filters.bytes)
    module.filter("bps", Filters.bps)

    // add the controllers and services
    configureServices(module)
    configureFactories(module)
    configureControllers(module)

    // define the routes
    module.config({ ($routeProvider: RouteProvider) =>
      // configure the routes
      $routeProvider
        .when("/activity", new RouteTo(templateUrl = "/views/activity/index.html", controller = classOf[ActivityController].getSimpleName))
        .when("/dashboard", new RouteTo(templateUrl = "/views/dashboard/index.html", controller = classOf[DashboardController].getSimpleName))
        .when("/dashboard/:id", new RouteTo(templateUrl = "/views/dashboard/index.html", controller = classOf[DashboardController].getSimpleName))
        .when("/slaves", new RouteTo(templateUrl = "/views/slaves/index.html", controller = classOf[SlaveController].getSimpleName))
        .when("/triggers", new RouteTo(templateUrl = "/views/triggers/index.html", controller = classOf[TriggerController].getSimpleName))
        .when("/workflows", new RouteTo(templateUrl = "/views/workflows/index.html", controller = classOf[WorkflowController].getSimpleName))
        .otherwise(new RouteTo(redirectTo = "/activity"))
      ()
    })

    // initialize the application
    module.run({ ($rootScope: Scope, $timeout: Timeout, WebSocketService: WebSocketService) =>
      // initialize the web socket service
      WebSocketService.init()
    })
  }

  private def configureControllers(module: Module) {
    module.controllerOf[ActivityController]("ActivityController")
    module.controllerOf[DashboardController]("DashboardController")
    module.controllerOf[MainController]("MainController")
    module.controllerOf[SlaveController]("SlaveController")
    module.controllerOf[TriggerController]("TriggerController")
    module.controllerOf[WorkflowController]("WorkflowController")
  }

  private def configureFactories(module: Module): Unit = {
    //module.factoryOf[UserFactory]("UserFactory")
  }

  private def configureServices(module: Module) {
    module.serviceOf[JobService]("JobService")
    module.serviceOf[WebSocketService]("WebSocketService")
    module.serviceOf[WorkflowService]("WorkflowService")
  }

}
