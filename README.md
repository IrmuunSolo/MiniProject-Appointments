# 4-р өдөр: Мини төслийн загварчлал ба хөгжүүлэлтийн эхлэл

Зорилго: Мини төслийн классын загварыг боловсруулж, хөгжүүлэлтийг эхлүүлэх.

## Odoo.com-ийн модулиудыг судалгаа

**Appointment App (Odoo-д суурилсан)**

Odoo-ын онцлог:

- Appointment модуль нь Odoo-ын стандарт form, tree, kanban харагдацуудыг ашигладаг.
- appointment.invite модель нь Odoo-ын календар системтэй интеграцдэг.
- Имэйл урилга илгээхэд Odoo-ын mail модультэй холбогддог.
- Зарим цагийн төрөлд төлбөр нэхэмжлэхтэй холбоотой бол account модультэй интеграцдэг.
- ir.rule ашиглан хандах эрхийг зохицуулдаг.

Odoo-ын бусад модулиудтай харилцууд:

- CRM: Захиалгатай холбоотой худалдан авагчид CRM-д бүртгэгддэг
- Project: Зарим цагийн захиалгууд нь тодорхой төслүүдтэй холбогддог.
- Website: Цагийн хуваарийг вэбсайтаар захиалах боломжтой

## Төслийн загварчлал:

3-р өдрийн шинжилгээгээ ашиглан классын загварын боловсруулалт.

![UML diagram](/images/AppointmentUML.drawio.png)

**1. Инкапсуляци (Encapsulation)**

Өгөгдөл болон үйлдлүүдийг нэг нэгжид багтааж, гаднаас шууд хандахаас хамгаална.

Жишээ:

- Appointment, Company, Service, Professional зэрэг классуудад private эсвэл protected гишүүдийг ашиглаж, getter/setter-ээр хандалтыг зохицуулсан.

```
private int id;
private String name;
public int getId() { return id; }
```

**2. Удамшил (Inheritance)**

Нэг классын шинж чанар, үйлдлийг өөр класст өвлүүлэн ашиглах.

Жишээ:

Person → Client, Professional хоёр унаган класс байна:

```
public class Client extends Person
public class Professional extends Person
```

Ийм байдлаар id, name, phone, email зэрэг давхардсан шинж чанарыг дахин бичилгүйгээр ашиглаж байгаа нь кодын давтагдлыг бууруулсан.

**3. Полиморфизм (Polymorphism)**

Нэг ижил нэртэй функц олон төрлийн хэрэгжилттэй байх (override/overload).

Жишээ:

toString() функц нь Person, Client, Professional, Appointment, Company, Service зэрэг олон класст override хийгдсэн.

```
@Override
public String toString() {
    return "Client{" + ...;
}
```

## Сайжруулсан үндсэн классууд ба аттрибут, функцийн тайлбар:


**1. Professional – Үйлчилгээ үзүүлэгч**

Атрибутууд:

```
id – Үйлчилгээ үзүүлэгчийн ID (Person superclass-аас өвлөсөн)
name – Нэр (Person superclass-аас өвлөсөн)
phone, email – Холбоо барих мэдээлэл (Person superclass-аас)
specialty – Мэргэшил (ж: "Сэтгэл зүйч")
rating – Үнэлгээ (0–5 хооронд)
pricePerHour – Цагийн үнэ (₮)
company – Харьяалагддаг байгууллага (Company обьект)
```


**2. Service – Үйлчилгээ**

Атрибутууд:

```
id – Үйлчилгээний ID
name – Нэр (жишээ нь: "Сэтгэл зүйн зөвлөгөө")
description – Үйлчилгээний дэлгэрэнгүй тайлбар
professionals – Уг үйлчилгээг үзүүлж чадах мэргэжилтнүүдийн жагсаалт (List<Professional>)
defaultDurationHours – Үндсэн үйлчилгээний үргэлжлэх хугацаа (цаг)
```

Функцууд:


```
addProfessional(pro) – Мэргэжилтэн нэмэх
removeProfessional(pro) – Мэргэжилтэн хасах
isOfferedBy(pro) – Уг мэргэжилтэн энэ үйлчилгээг үзүүлдэг эсэх
```

**3. Client – Хэрэглэгч**

Person классаас удамшсан.

Атрибутууд:

```
id, name, phone, email – Person superclass-аас
```

**4. Appointment – Захиалга**

Атрибутууд:

```
id – Захиалгын ID
client – Захиалга хийж буй хэрэглэгч (Client)
professional – Үйлчилгээ үзүүлэгч (Professional)
service – Сонгосон үйлчилгээ (Service)
date – Захиалгын огноо (LocalDate)
startHour – Эхлэх цаг (ажлын цаг: 9–17)
durationHours – Үргэлжлэх хугацаа (цаг)
isOnline – Онлайн эсэх (true бол онлайн, false бол биечлэн)
notes – Нэмэлт тэмдэглэл
```

Функцууд:

```
isOnline() – Захиалга онлайнаар явагдах эсэхийг шалгах
getLocation() – Онлайн бол “Online”, үгүй бол компанийн хаяг
calculateFeeByDuration() – Үргэлжлэх хугацаагаар нийт төлбөрийг тооцох
calculateFeeByPayment() – Цагийн төлбөрөөр нэгж үнэ тооцох
setNotes(), setOnline() – Захиалгын мэдээллийг шинэчлэх
```

**5. AppointmentSystem – Захиалгын систем**

Атрибутууд:

```
schedules – Мэргэжилтний цагийн хуваарийг хадгалах бүтэц
Map<Professional, Map<LocalDate, boolean[]>> – Тухайн өдрийн 9–17 цагийн (8 цаг) төлөв
appointments – Захиалгуудын жагсаалт
```

Функцууд:

```
registerProfessional(professional) – Мэргэжилтэн бүртгэх
initializeDay(professional, date) – Мэргэжилтний тухайн өдрийн бүх цагийг сул болгох
isAvailable(professional, date, hour) – Тухайн цаг сул эсэхийг шалгах
bookAppointment(...) – Захиалга үүсгэх, олон цагийг зэрэглэх боломжтой
(ирээдүйд хэрэгжих функцүүд):
cancelAppointment() – Захиалгыг цуцлах
getClientAppointments() – Хэрэглэгчийн захиалгууд
getProsAppointments() – Мэргэжилтний захиалгууд
getAvailableHours() – Сул цагуудыг харуулах
```

**6. Company – Байгууллага**

Атрибутууд:

```
id, name, address, phone, email – Байгууллагын мэдээлэл
(Professional класстай холбогддог)
```

**7. Person – Хувь хүн (Superclass)**

Атрибутууд:

```
id – Хувь хүний ID
name – Нэр
phone – Утас
email – Имэйл
Өвлөгчид: Client, Professional
```

## Pull request and merge

![pull request](/images/4.day4pullrequest.png)


**Merged**

![pull request](/images/4.merge.png)
