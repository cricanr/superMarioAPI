import com.google.inject.AbstractModule
import csvparser.{ISuperMarioCharactersParser, SuperMarioCharactersParser}
import net.codingwell.scalaguice.ScalaModule
import services.{ISuperMarioCharactersService, SuperMarioCharactersService}

import java.time.Clock

/** This class is a Guice module that tells Guice how to bind several
  * different types. This Guice module is created when the Play
  * application starts.
  *
  * Play will automatically use any class called `Module` that is in
  * the root package. You can create modules in other locations by
  * adding `play.modules.enabled` settings to the `application.conf`
  * configuration file.
  */
class Module extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)
    bind[ISuperMarioCharactersService]
      .to[SuperMarioCharactersService]
      .asEagerSingleton()
    bind[ISuperMarioCharactersParser]
      .to[SuperMarioCharactersParser]
      .asEagerSingleton()
  }
}
