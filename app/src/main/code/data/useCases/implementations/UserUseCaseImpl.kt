package data.useCases.implementations

import data.mappers.interfaces.UserMapper
import data.repositories.interfaces.UserRepository
import data.useCases.interfaces.UserUseCase
import domain.models.entities.User
import domain.models.responses.TokenWrapper
import domain.models.values.Gender
import io.reactivex.Completable
import io.reactivex.Single

/**
 * @author Keyrillanskiy
 * @since 18.01.2019, 21:05.
 */
class UserUseCaseImpl(private val userRepository: UserRepository, private val userMapper: UserMapper) : UserUseCase {

    override fun authorize(token: TokenWrapper): Single<User> {
        return userRepository.authorize(token)
            .map { userResponse -> userMapper.toUser(userResponse) }
    }

    override fun setGender(userId: String, gender: Gender): Completable = userRepository.setGender(userId, gender)

}