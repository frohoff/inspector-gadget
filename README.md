inspector-gadget
================

![Build Status](https://api.travis-ci.org/frohoff/inspector-gadget.svg?branch=master)

```
$ mvn -q clean package exec:java -Dexec.mainClass=org.frohoff.inspectorgadget.IndexApp
Start
Done parsing

         \,,,/
         (o o)
-----oOOo-(_)-oOOo-----
gremlin> g.V.and(_().out("implements").has("id","java.io.Serializable")).out('method').has('name','readObject').out('calls').path
==>[v[javax.sql.rowset.serial.SerialBlob], v[javax.sql.rowset.serial.SerialBlob.readObject(java.io.ObjectInputStream)], v[java.io.ObjectInputStream.readFields()]]
==>[v[javax.sql.rowset.serial.SerialBlob], v[javax.sql.rowset.serial.SerialBlob.readObject(java.io.ObjectInputStream)], v[java.io.ObjectInputStream$GetField.get(java.lang.String,java.lang.Object)]]
==>[v[javax.sql.rowset.serial.SerialBlob], v[javax.sql.rowset.serial.SerialBlob.readObject(java.io.ObjectInputStream)], v[java.io.ObjectInputStream$GetField.get(java.lang.String,long)]]
...
```
