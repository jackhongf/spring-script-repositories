package com.haulmont.scripting.scripts

import com.haulmont.scripting.core.test.files.Customer

class CustomerImplMixed implements Customer{

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
}

CustomerImplMixed c = new CustomerImplMixed()
c.setId(UUID.randomUUID())
c.setName(name)
c.setBirthDate(birthDate)
c