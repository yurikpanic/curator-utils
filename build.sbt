name          := """curator-utils"""
organization  := "com.elsyton"
version       := "1.0.0"

scalaVersion := "2.11.8"
scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature")

libraryDependencies ++= {
  val curatorV        = "2.11.0"
  Seq(
    "org.apache.curator"        %  "curator-framework"                    % curatorV
  )
}
