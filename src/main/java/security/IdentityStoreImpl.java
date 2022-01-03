package security;

import persistence.model.User;
import persistence.repo.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.CallerOnlyCredential;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.Set;

@ApplicationScoped
public class IdentityStoreImpl implements IdentityStore {

    @Inject
    private UserRepository userRepository;

    @Inject
    private Pbkdf2PasswordHashImpl passwordHash;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {
            return validate((UsernamePasswordCredential) credential);
        } else if (credential instanceof CallerOnlyCredential) {
            return validate((CallerOnlyCredential) credential);
        } else {
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }
    }

    public CredentialValidationResult validate(UsernamePasswordCredential credential) {
        User user = userRepository.getByName(credential.getCaller());

        if(user != null && passwordHash.verify(credential.getPasswordAsString(), user.getPassword(), user.getSalt()))
            return new CredentialValidationResult(
                    credential.getCaller(),
                    user.getAdmin() ? Set.of("user", "admin") : Set.of("user"));
        return CredentialValidationResult.INVALID_RESULT;
    }

    public CredentialValidationResult validate(CallerOnlyCredential credential) {
        User user = userRepository.getByName(credential.getCaller());

        if(user != null)
            return new CredentialValidationResult(
                    credential.getCaller(),
                    user.getAdmin() ? Set.of("user", "admin") : Set.of("user"));
        return CredentialValidationResult.INVALID_RESULT;
    }

    @Override
    public int priority() {
        return 1;
    }
}
