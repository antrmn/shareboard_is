package http.auth;

import service.dto.UserIdentityDTO;
import service.IdentityProviderService;

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
    private IdentityProviderService idpService;


    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {
            System.out.println(((UsernamePasswordCredential) credential).getCaller() + "," +((UsernamePasswordCredential) credential).getPasswordAsString());

            CredentialValidationResult validate = validate((UsernamePasswordCredential) credential);
            System.out.println(validate.getStatus().name());
            return validate;
        } else if (credential instanceof CallerOnlyCredential) {
            System.out.println(((CallerOnlyCredential) credential).getCaller());
            CredentialValidationResult validate = validate((CallerOnlyCredential) credential);
            System.out.println(validate);
            return validate;
        } else {
            System.out.println("niente");
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }
    }

    public CredentialValidationResult validate(UsernamePasswordCredential credential) {
        UserIdentityDTO identity = idpService.getUserIdentity(credential.getCaller());
        if(identity != null && idpService.checkCredentials(credential.getCaller(), credential.getPasswordAsString()))
            return new CredentialValidationResult(credential.getCaller(), identity.isAdmin() ? Set.of("user", "admin") : Set.of("user"));
        return CredentialValidationResult.INVALID_RESULT;
    }

    public CredentialValidationResult validate(CallerOnlyCredential credential) {
        UserIdentityDTO identity = idpService.getUserIdentity(credential.getCaller());
        if(identity != null)
            return new CredentialValidationResult(credential.getCaller(), identity.isAdmin() ? Set.of("user", "admin") : Set.of("user"));
        return CredentialValidationResult.INVALID_RESULT;
    }

    @Override
    public int priority() {
        return 1;
    }
}
