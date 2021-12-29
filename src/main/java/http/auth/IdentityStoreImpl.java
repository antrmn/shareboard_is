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
            return validate((UsernamePasswordCredential) credential);
        } else if (credential instanceof CallerOnlyCredential) {
            return validate((CallerOnlyCredential) credential);
        } else {
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }
    }

    public CredentialValidationResult validate(UsernamePasswordCredential credential) {
        UserIdentityDTO identity = idpService.getUserIdentity(credential.getCaller());
        if(identity != null && idpService.checkCredentials(credential.getCaller(), credential.getPasswordAsString()))
            return new CredentialValidationResult(null, identity.getUsername(), null,
                    String.valueOf(identity.getId()), identity.isAdmin() ? Set.of("user", "admin") : Set.of("user"));
        return CredentialValidationResult.INVALID_RESULT;
    }

    public CredentialValidationResult validate(CallerOnlyCredential credential) {
        UserIdentityDTO identity = idpService.getUserIdentity(credential.getCaller());
        if(identity != null)
            return new CredentialValidationResult(null, identity.getUsername(), null,
                        String.valueOf(identity.getId()), identity.isAdmin() ? Set.of("user", "admin") : Set.of("user"));
        return CredentialValidationResult.INVALID_RESULT;
    }

    @Override
    public int priority() {
        return 9999999;
    }
}
