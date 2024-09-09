# susu-api (в разработке)

# RU 
<details>
<summary>Кликните, чтобы развернуть</summary>

## Оглавление
1️⃣ [Описание API](#api)

2️⃣ [Точки доступа (эндпоинты)](#endpoints)

3️⃣ [Подробнее о каждой точке доступа](#about-each-endpoint)

4️⃣ [Возможные коды ответов API](#api-codes)

### <a name="api">📝 Описание API</a>
* Данное API создано для взаимодействия с сервисами ЮУрГУ, среди которых: studlk.susu.ru, edu.susu.ru, online.susu.ru. 
* API доступно для взаимодействия только студентам ЮУрГУ, **пароли от учётных записях не собираются, не хранятся и не передаются.**
* Интерфейс состоит только из POST запросов, так как для взаимодействия с сервисами ЮУрГУ необходимо пройти аутентификацию. 
* API использует стиль именования переменных snake-case, однако некоторые ответы содержат переменные в стиле PascalCase. Это различие обусловлено особенностями формата данных, предоставляемых ЮУрГУ, а не самим API.

---
---

### <a name="endpoints">🌐 Точки доступа</a>
* 🍪 [Куки сессии:](#about-cookies) `POST/susu-api/authentication`
* 🎓 [Итоговые оценки:](#about-grades) `POST/susu-api/grades`
* 📊 [Процентная успеваемость:](#about-percentage) `POST/susu-api/percentage`
* 📜 [Учебный план:](#about-study-plan) `POST/susu-api/study-plan`
* 📘 [Рабочая программа дисциплины (РПД):](#about-rpd) `POST/susu-api/rpd`
* 🗓️ [Расписание:](#about-schedule) `POST/susu-api/schedule`
* 📚 [Читательский билет:](#about-library-card) `POST/susu-api/library-card`
* 🛂 [Пропуск:](#about-badge) `POST/susu-api/badge`

---
---

### <a name="about-each-endpoint">Подробнее о каждой точке доступа</a>

#### 🍪 Куки сессии: <a name="about-cookies">`POST/susu-api/authentication'

Данная точка доступа (эндпоинт) предназначена для получения куки сессии, использование которых позволяет выполнять HTTP-запросы без необходимости постоянной повторной аутентификации, что значительно ускоряет обработку запросов и улучшает отклик сервера.

**Тело запроса:**
```json
{
  "username": "YOUR_SUSU_ACCOUNT_USERNAME",
  "password": "YOUR_SUSU_ACCOUNT_PASSWORD",
  "type": "studlk.susu.ru"
}
```

**Параметры запроса:**

- `type` — типы куки:
  - **`studlk.susu.ru`**: возвращает куки для studlk.susu.ru
  - **`online.susu.ru`**: возвращает куки для online.susu.ru
  - **`edu.susu.ru`**: возвращает куки для edu.susu.ru

**Тело ответа:**
```json
{
  "cookie": "SESSION_COOKIE"
}
```

<a href="#endpoints">⬆️</a>

---

#### 🎓 Итоговые оценки: <a name="about-grades">`POST/susu-api/grades`</a>

Данная точка доступа (эндпоинт) предназначена для получения итоговых оценок пользователя в формате JSON.

**Тело запроса:**
```json
{
  "username": "YOUR_SUSU_ACCOUNT_USERNAME",
  "password": "YOUR_SUSU_ACCOUNT_PASSWORD"
}
```

**Тело ответа:**
```json
[
  {
    "DisciplineName": "Основы геймплея игры Rust",
    "DisciplineId": "96f4083f-891e-47b6-abef-2357d56d0215",
    "TermNumber": 8,
    "Date": "2021-12-21T02:03:21.9341",
    "ControlType": "экзамен",
    "Mark": 4,
    "IsReexam": false,
    "ReexamText": "",
    "CheckId": "d47e3102-0562-412c-9008-cee3c5a6693a",
    "MarkNumber": 132562
  }
]
```

**Параметры ответа:**

- `DisciplineId` — идентификатор дисциплины, используемый в РПД. Данный идентификатор может повторяться у разных пользователей
> 📝 **Пример:** https://studlk.susu.ru/ru/Reference/SubjectProgram/96f4083f-891e-47b6-abef-2357d56d0215?discType=RPD

- `TermNumber` — семестр к которому относится дисциплина

- `Date` — дата выставления оценки/зачёта

- `ControlType` — тип контроля, возможны следующие значения: **экзамен**, **дифференцированный зачет**, **зачет**, **аттестация**, **курсовая работа**

- `IsReexam` — указывает на то, пересдавалась ли дисциплина

- `ReexamText` — комментарий после пересдачи

- `CheckId` — идентификатор записи в личном кабинете. Данный идентификатор уникален для каждой записи

> 💡 **Пояснение:** запись — это JSON, который вы получаете при `POST/susu-api/grades` запросе

- `MarkNumber` — идентификатор оценки, скорее всего относится к ведомости. Данный индентификатор уникален для каждой оценки

<a href="#endpoints">⬆️</a>

---

#### 📊 Процентная успеваемость: <a name="about-percentage">`POST/susu-api/percentage`</a>

Данная точка доступа (эндпоинт) предназначена для получения процентной успеваемости пользователя в формате JSON.

**Тело запроса:**
```json
{
  "username": "YOUR_SUSU_ACCOUNT_USERNAME",
  "password": "YOUR_SUSU_ACCOUNT_PASSWORD",
  "mode": "total"
}
```

**Параметры запроса:**

- `mode` — режим запроса, может принимать следующие значения:
  - **`total`**: возвращает общую процентную успеваемость пользователя
  - **`by-subject`**: возвращает процентную успеваемость по каждому предмету

**Тело ответа `"total"`:**
```json
{
  "first-semester-percentage": 72.12,
  "total-percentage": 74.37
}
```

**Параметры ответа:**

- `first-semester-percentage` — процент успеваемости за первый семестр
- `total-percentage` — общая процентная успеваемость за текущий учебный год

**Тело ответа `"by-subject"`:**
```json
[
  {
    "Name:": "Физическая культура и спорт",
    "TermNumber": 10,
    "Rating": 92.45
  }
]
```

**Параметры ответа:**
- `Name` — название дисциплины
- `TermNumber` — семестр к которому относится дисциплина
- `Rating` — процентная успеваемость (рейтинг) по дисциплине

<a href="#endpoints">⬆️</a>

---

#### 📜 Учебный план <a name="about-study-plan">`POST/susu-api/study-plan`</a>

Данная точка доступа (эндпоинт) предназначена для получения учебного плана пользователя в формате JSON.

**Тело запроса:**
```json
{
  "username": "YOUR_SUSU_ACCOUNT_USERNAME",
  "password": "YOUR_SUSU_ACCOUNT_PASSWORD"
}
```

**Тело ответа:**
```json
[
  {
    "Id": "2cdbcfb3-97c3-4eb2-9dc0-1ed49fd2532d",
    "SubjectId": "00000000-0000-0000-0000-000000000000",
    "disciplineType": 0,
    "JournalId": "72ccfcf1-ebb9-43e0-a015-2441fdbc5a09",
    "TermNumber": 9,
    "CycleName": " ",
    "DisciplineName": "Физическая культура и спорт",
    "CathedraName": "Физическое воспитание и здоровье",
    "CathedraId": "99648a6d-cd90-42f0-95b7-7c515dbd4c91",
    "AllHours": 124,
    "LectureHours": 31,
    "PracticeHours": 61,
    "LabHours": 0,
    "SelfStudyHours": 31,
    "CourseWork": false,
    "CourseProject": false,
    "Credit": false, 
    "DifCredit": false, 
    "Exam": false, 
    "Passed": false,
    "SubjectName": "Физическая культура и спорт",
    "RawSubjectId": "76b4ba17-7067-468a-b6c2-8c681b56a454", 
    "BaseSubject": null
  }
]
```

**Параметры ответа:**

- `Id` — идентификатор дисциплины, используемый в РПД. Данный идентификатор может повторяться у разных пользователей

> 📝 **Пример:** https://studlk.susu.ru/ru/Reference/SubjectProgram/2cdbcfb3-97c3-4eb2-9dc0-1ed49fd2532d?discType=RPD

- `JournalId` — идентификатор журнала с оценками для выбранной дисциплины

> 📝 **Пример:** https://studlk.susu.ru/ru/StudyPlan/OpenJournal/72ccfcf1-ebb9-43e0-a015-2441fdbc5a09

- `TermNumber` — семестр к которому относится дисциплина
- `LabHours` — часы отведённые на лабораторные работы по выбранной дисциплине
- `SelfStudyHours` — часы отведённые на самостоятельную работу по выбранной дисциплине
- `CourseWork` — наличие/отсутствие курсовой работы по выбранной дисциплине
- `CourseProject` — наличие/отсутствие проекта по выбранной дисциплине
- `Exam` — наличие/отсутствие экзамена по выбранной дисциплине 

<a href="#endpoints">⬆️</a>

---
---

### <a name="api-codes"> 🛠️ Возможные коды ответов API:</a>
| Код | Описание                                                        |
|-----|-----------------------------------------------------------------|
| 200 | OK — запрос успешно обработан                                   |
| 400 | Bad Request — некорректный запрос                               |
| 404 | Not Found — несуществующая точка доступа                        |
| 429 | Too Many Requests — превышено количество запросов               |
| 500 | Server Error — ошибка на стороне сервера (500, 502, 503, 504)   |

</details>
