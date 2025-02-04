/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.feel.api.valuemapper

import java.util
import java.util.Collections

import org.camunda.feel.FeelEngine
import org.camunda.feel.context.Context
import org.camunda.feel.impl.JavaValueMapper
import org.camunda.feel.valuemapper.{SimpleBooleanTestPojo, SimpleTestPojo}
import org.camunda.feel.valuemapper.ValueMapper.CompositeValueMapper
import org.scalatest.matchers.should.Matchers
import org.scalatest.flatspec.AnyFlatSpec

class BuiltinValueMapperInputTest extends AnyFlatSpec with Matchers {

  val engine =
    new FeelEngine(null, CompositeValueMapper(List(new JavaValueMapper())))

  "The value mapper" should "read java.lang.String" in {
    val variables = Map("foo" -> java.lang.String.valueOf("3.4"))

    engine
      .evalExpression("foo + \"hello\"",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe a[java.lang.String]
  }

  it should "read java.lang.Float" in {
    val variables = Map("foo" -> java.lang.Float.valueOf("3.4"))

    engine
      .evalExpression("foo + 1",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe a[java.lang.Double]
  }

  it should "read java.lang.Double" in {
    val variables = Map("foo" -> java.lang.Double.valueOf("3.4"))

    engine
      .evalExpression("foo + 1",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe a[java.lang.Double]
  }

  it should "read java.lang.Integer" in {
    val variables = Map("foo" -> java.lang.Integer.valueOf("3"))

    engine
      .evalExpression("foo + 1",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe a[java.lang.Long]
  }

  it should "read java.lang.Long" in {
    val variables = Map("foo" -> java.lang.Long.valueOf("3"))

    engine
      .evalExpression("foo + 1",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe a[java.lang.Long]
  }

  it should "read java.lang.Short" in {
    val variables = Map("foo" -> java.lang.Short.valueOf("3"))

    engine
      .evalExpression("foo + 1",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe a[java.lang.Long]
  }

  it should "read java.lang.Boolean" in {
    val variables = Map("foo" -> java.lang.Boolean.valueOf("true"))

    engine
      .evalExpression("foo or false",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe a[java.lang.Boolean]
  }

  it should "read java.util.Date" in {
    val variables = Map("foo" -> new java.util.Date())

    engine
      .evalExpression("foo", context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe a[java.time.LocalDateTime]
  }

  it should "read null value" in {
    val nullValue: java.lang.Integer = null

    val variables = Map("foo" -> nullValue)

    engine
      .evalExpression("foo = null",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe true
  }

  it should "read value from object getter" in {

    val variables = Map("foo" -> new SimpleTestPojo("foo"))

    engine
      .evalExpression("foo.getMyString() = \"foo\"",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe true
  }

  it should "read value from object getter with attribute notation" in {

    val variables = Map("foo" -> new SimpleTestPojo("foo"))

    engine
      .evalExpression("foo.myString = \"foo\"",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe true
  }

  it should "read boolean 'true' from object 'is...' getter" in {
    val pojo = new SimpleBooleanTestPojo()
    pojo.setEnabled(true)

    val variables = Map("foo" -> pojo)

    engine
      .evalExpression("foo.isEnabled() = true",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe true
  }

  it should "read boolean 'true' from object getter with attribute notation" in {
    val pojo = new SimpleBooleanTestPojo()
    pojo.setEnabled(true)

    val variables = Map("foo" -> pojo)

    engine
      .evalExpression("foo.enabled = true",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe true
  }

  it should "read boolean 'false' from object getter with attribute notation" in {
    val pojo = new SimpleBooleanTestPojo()
    pojo.setEnabled(false)

    val variables = Map("foo" -> pojo)

    engine
      .evalExpression("foo.enabled = false",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe true
  }

  it should "read boolean 'true' from object getter" in {
    val pojo = new SimpleBooleanTestPojo()
    pojo.setDisabled(true)

    val variables = Map("foo" -> pojo)

    engine
      .evalExpression("foo.getDisabled() = true",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe true
  }

  it should "read boolean 'true' from object getter attribute notation" in {
    val pojo = new SimpleBooleanTestPojo()
    pojo.setDisabled(true)

    val variables = Map("foo" -> pojo)

    engine
      .evalExpression("foo.disabled = true",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe true
  }

  it should "read string from object 'is...' getter" in {
    val pojo = new SimpleBooleanTestPojo()
    pojo.setFoo("baz")

    val variables = Map("bar" -> pojo)

    engine
      .evalExpression("bar.isFoo() = \"baz\"",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe true
  }

  it should "not read string from object 'is...' getter with attribute notation" in {
    val pojo = new SimpleBooleanTestPojo()
    pojo.setFoo("baz")

    val variables = Map("bar" -> pojo)

    engine
      .evalExpression("bar.foo = \"baz\"",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe ()
  }

  it should "read scala string from object getter with attribute notation" in {
    val pojo = new MyScalaType()

    val variables = Map("bar" -> pojo)

    engine
      .evalExpression("bar.foo = \"baz\"",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe true
  }

  it should "read scala boolean 'true' from object getter" in {
    val pojo = new MyScalaType()

    val variables = Map("bar" -> pojo)

    engine
      .evalExpression("bar.getEnabled() = true",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe true
  }

  it should "read scala boolean 'true' from object getter with attribute notation" in {
    val pojo = new MyScalaType()

    val variables = Map("bar" -> pojo)

    engine
      .evalExpression("bar.enabled = true",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe true
  }

  it should "read scala boolean 'false' from object IS getter" in {
    val pojo = new MyScalaType()

    val variables = Map("bar" -> pojo)

    engine
      .evalExpression("bar.isDisabled = false",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe true
  }

  it should "read scala boolean 'false' from object IS getter with attribute notation" in {
    val pojo = new MyScalaType()

    val variables = Map("bar" -> pojo)

    engine
      .evalExpression("bar.disabled = false",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe true
  }

  it should "read scala string from object IS getter with attribute notation" in {
    val pojo = new MyScalaType()

    val variables = Map("bar" -> pojo)

    engine
      .evalExpression("bar.isBaz = \"foo\"",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe true
  }

  it should "not read scala string from object IS getter with attribute notation" in {
    val pojo = new MyScalaType()

    val variables = Map("bar" -> pojo)

    engine
      .evalExpression("bar.baz = \"foo\"",
                      context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe ()
  }

  it should "read java.util.Map" in {
    val map: java.util.Map[java.lang.String, java.lang.Object] =
      new util.HashMap[java.lang.String, java.lang.Object]()
    map.put("foo", "myString")
    map.put("bar", java.lang.Integer.valueOf(2))
    map.put("baz", Collections.singletonMap("foo", "bar"))

    val variables = Map("map" -> map)

    engine
      .evalExpression(
        "map.foo = \"myString\" and map.bar = 2 and map.baz.foo = \"bar\"",
        context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe true
  }

  it should "read java.util.List" in {
    val list: java.util.List[java.lang.Object] =
      new util.ArrayList[java.lang.Object]()
    list.add("myString")
    list.add(java.lang.Integer.valueOf(2))
    list.add(Collections.singletonMap("foo", "bar"))

    val variables = Map("list" -> list)

    engine
      .evalExpression(
        "list[1] = \"myString\" and list[2] = 2 and list[3].foo = \"bar\"",
        context = Context.StaticContext(variables, null))
      .getOrElse() shouldBe true
  }

  class MyScalaType {
    private val foo: String = "baz"
    private val baz: String = "foo"
    private val enabled: Boolean = true
    private val disabled = false

    def getFoo: String = foo
    def isBaz: String = baz
    def getEnabled: Boolean = enabled
    def isDisabled = disabled
  }
}
