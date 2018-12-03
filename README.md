# Ручное построение нисходящих синтаксических анализаторов

## Регулярные выражения

Регулярные выражения с операциями конкатенации (простая последовательная
запись строк), выбора (вертикальная черта), замыкания
Клини. Приоритет операций стандартный. Скобки могут использоваться
для изменения приоритета.

Для обозначения базовых языков используются маленькие буквы латинского
алфавита. Используйте один терминал для всех символов.

Пример: `((abc*b|a)*ab(aa|b*)b)*`

## 1. Разработка грамматики

> Reg -> Reg|Choice  
> Reg -> ε  
> Reg -> Choice  
> Choice -> ChoicePart  
> Choice -> ε  
> Part -> EntityKleene  
> Kleene -> ε  
> Kleene -> *  
> Entity -> [a..z]  
> Entity -> (Reg)  

Нетерминал | Описание
--- | ---
*Reg* | Регулярное выражение 
*Choice* | Последнее выражение, отделенное операцией выбора
*Part* | Последнее выражение, отделенное операцией конкатенации c замыканием Клини или без
*Kleene* | Операция замыкания Клини или пустая строка
*Entity* | Выражение в скобках или буква

В грамматике есть левые рекурсии. Устраним их. Получится грамматика:

> Reg -> ChoiceReg'  
> Reg -> Choice  
> Reg -> ε  
> Reg -> Reg'  
> Reg' -> |ChoiceReg'  
> Reg' -> |Choice  
> Choice -> PartChoice  
> Choice -> ε   
> Part -> EntityKleene  
> Kleene -> ε  
> Kleene -> *  
> Entity -> [a..z]  
> Entity -> (Reg)  

Нетерминал | Описание
--- | ---
*Reg* | Регулярное выражение 
*Choice* | Последнее выражение, отделенное операцией выбора
*Part* | Последнее выражение, отделенное операцией конкатенации c замыканием Клини или без
*Kleene* | Операция замыкания Клини или пустая строка
*Entity* | Выражение в скобках или буква
*Reg'* | Продолжение регулярного выражения 

В грамматике есть правые ветвления. Устраним их. Получится грамматика:

> Reg -> ChoiceReg'  
> Reg -> Reg'  
> Reg' -> |ChoiceReg'  
> Reg' -> ε  
> Choice -> PartChoice  
> Choice -> ε   
> Part -> EntityKleene  
> Kleene -> ε  
> Kleene -> *  
> Entity -> [a..z]  
> Entity -> (Reg) 

Нетерминал | Описание
--- | ---
*Reg* | Регулярное выражение 
*Choice* | Последнее выражение, отделенное операцией выбора
*Part* | Последнее выражение, отделенное операцией конкатенации c замыканием Клини или без
*Kleene* | Операция замыкания Клини или пустая строка
*Entity* | Выражение в скобках или буква
*Reg'* | Продолжение регулярного выражения 

## 2. Построение лексического анализатора

Лексический анализатор должен получать на вход строку и выдавать
последовательность терминалов (токенов). Пробелы и переводы строк
должны игнорироваться.

Терминал | Токен
--- | ---
[a..z] | *LETTER*
&#124; | *BAR*
\* | *STAR*
*(* | *OPEN*
*)* | *CLOSE*
*$* | *END*

## 3. Построение синтаксического анализатора

| Нетерминал | FIRST                 | FOLLOW                        |
| ---------- |---------------------- | ----------------------------- |
| *Reg*      | `(` `\|` `[a..z]` `ε` | `)` `$`                       |
| *Choice*   | `(` `[a..z]` `ε`      | `)` `\|` `$`                  |
| *Part*     | `(` `[a..z]`          | `(` `\|` `[a..z]` `)` `$`     |
| *Kleene*   | `*` `ε`               | `(` `\|` `[a..z]` `)` `$`     |
| *Entity*   | `(` `[a..z]`          | `*` `(` `\|` `[a..z]` `)` `$` |
| *Reg'*     | `\|` `ε`              | `)` `$`                       |