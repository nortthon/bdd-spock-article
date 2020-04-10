package io.github.nortthon.articles.usecases

import io.github.nortthon.articles.domains.Credential
import io.github.nortthon.articles.gateways.UserGateway
import spock.lang.Specification

class UpdatePasswordTest extends Specification {

    def userGateway = Mock(UserGateway)

    def "The user's password must be changed when prompted"() {
        given: "a user logged in as leonidas@sparta.rg"
        def user = "leonidas@sparta.rg"
        userGateway.find(_ as String) >> new Credential(user: user, password: "1234ABCD")
        and: "want to change the password to 9A8B12ZA"
        def newPassword = "9A8B12ZA"
        userGateway.save(_ as Credential) >> new Credential(user: user, password: newPassword)

        when: "he requests to change to this new password"
        def useCase = new UpdatePassword(userGateway)
        def result = useCase.execute(user, newPassword)

        then: "the password is successfully changed"
        assert result.user == user
        assert result.password == newPassword
    }

    def "The new password must not be the same as the current one"() {
        given: "a user logged in as leonidas@sparta.rg"
        def user = "leonidas@sparta.rg"
        and: "want to change the password aAb12345 for the same password"
        def newPassword = "aAb12345"
        userGateway.find(_ as String) >> new Credential(user: user, password: newPassword)

        when: "he requests the exchange for a password equal to the current one"
        def useCase = new UpdatePassword(userGateway)
        useCase.execute(user, newPassword)

        then: "the password is not changed"
        def e = thrown(IllegalArgumentException)
        and: "the user receives an error message stating that the password cannot be the same as the previous one"
        assert e.message == "the password must be different from the current one"
    }

    def "The new password must contain at least 8 characters"() {
        given: "a user logged in as leonidas@sparta.rg"
        def user = "leonidas@sparta.rg"
        and: "want to change the password to 12345A with less than 8 characters"
        def newPassword = "12345A"

        when: "he requests to change to this new password"
        def useCase = new UpdatePassword(userGateway)
        useCase.execute(user, newPassword)

        then: "the password is not changed"
        def e = thrown(IllegalArgumentException)
        and: "the user receives an error message stating that the password must be at least 8 characters"
        assert e.message == "the password must consist of numbers and letters, and at least 8 characters"
    }

    def "The new password must contain numbers and letters"() {
        given: "a user logged in as leonidas@sparta.rg"
        def user = "leonidas@sparta.rg"
        and: "want to change the password to 12345678 using only numbers"
        def newPassword = "12345678"

        when: "he requests to change to this new password"
        def useCase = new UpdatePassword(userGateway)
        useCase.execute(user, newPassword)

        then: "the password is not changed"
        def e = thrown(IllegalArgumentException)
        and: "the user receives an error message stating that the password must consist of numbers and letters"
        assert e.message == "the password must consist of numbers and letters, and at least 8 characters"
    }

}