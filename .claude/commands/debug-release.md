# Debug Release Issues

Systematically debug Maven Central publishing issues.

## Investigation Steps
1. Check JReleaser logs in build output
2. Verify PGP key distribution:
```bash
   gpg --keyserver keyserver.ubuntu.com --search-keys YOUR_KEY_ID
   gpg --keyserver keys.openpgp.org --search-keys YOUR_KEY_ID
   gpg --keyserver pgp.mit.edu --search-keys YOUR_KEY_ID
```
3. Validate gradle.properties versions
4. Check GitHub Actions logs
5. Verify nexus2.maven.org accessibility
6. Review JReleaser config output

## Common Issues & Solutions
- "immutableRelease is null": Version mismatch in gradle.properties
- PGP verification failed: Key not propagated (wait 5-10 mins)
- Staging repository not found: Profile name mismatch
- 401 Unauthorized: Check NEXUS credentials