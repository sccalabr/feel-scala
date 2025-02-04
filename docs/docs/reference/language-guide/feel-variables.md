---
id: feel-variables
title: Variables
description: "This document outlines variables and examples."
---

### Access variables

Access the value of a variable by its variable name.

```js
a + b
```

If the value of the variable is a context, a [context entry can be accessed](feel-context-expressions#get-entrypath) by its key. 

```js
a.b
```

:::tip

Use a [null-check](feel-boolean-expressions#null-check) if the variable can be `null` or is optional.

```js
a != null and a.b > 10 
```

:::

### Escape variable names

The name of a variable can be any alphanumeric string including `_` (an underscore). For a
combination of words, it is recommended to use the `camelCase` or the `snake_case` format.

If a variable name or context key contains any special character (e.g. whitespace, dash, etc.) then
the name can be wrapped into single backquotes/backticks (e.g. ``` `foo bar` ```).

```js
`first name`

`tracking-id`

order.`total price`
```
