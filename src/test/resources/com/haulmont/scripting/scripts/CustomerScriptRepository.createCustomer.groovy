package com.haulmont.scripting.scripts

import com.haulmont.scripting.core.test.files.Customer

class CustomerImpl implements Customer{

    private UUID id
    private String name
    private Date birthDate

    UUID getId() {
        return id
    }

    void setId(UUID id) {
        this.id = id
    }

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    Date getBirthDate() {
        return birthDate
    }

    void setBirthDate(Date birthDate) {
        this.birthDate = birthDate
    }

    @Override
    List<String> getMyData() {
        return [id.toString(), name, birthDate.toString()]
    }
}

CustomerImpl c = new CustomerImpl()
c.setId(UUID.randomUUID())
c.setName(name)
c.setBirthDate(birthDate)
c