# Security Policy

## Supported Versions

We actively support and provide security updates for the following versions of Proteus:

| Version | Supported          |
| ------- | ------------------ |
| 1.x.x   | :white_check_mark: |
| 0.x.x   | :x:                |

## Reporting Security Vulnerabilities

We take the security of Proteus seriously. If you discover a security vulnerability, please report it responsibly.

### How to Report

**Please do not report security vulnerabilities through public GitHub issues.**

Instead, please report security vulnerabilities by emailing:

📧 **mpetlyuk@gmail.com**

Include the following information in your report:

- Type of issue (e.g. buffer overflow, SQL injection, cross-site scripting, etc.)
- Full paths of source file(s) related to the manifestation of the issue
- The location of the affected source code (tag/branch/commit or direct URL)
- Any special configuration required to reproduce the issue
- Step-by-step instructions to reproduce the issue
- Proof-of-concept or exploit code (if possible)
- Impact of the issue, including how an attacker might exploit the issue

### Response Timeline

- **Initial Response**: Within 48 hours
- **Confirmation**: Within 7 days
- **Resolution**: Varies by severity and complexity

We will acknowledge your email within 48 hours and provide a more detailed response within 7 days indicating the next steps in handling your report.

## Security Best Practices

### For Developers

When contributing to Proteus, please follow these security guidelines:

1. **Input Validation**: Always validate and sanitize input data
2. **Dependencies**: Keep dependencies updated and audit for vulnerabilities
3. **Secrets**: Never commit secrets, API keys, or sensitive data
4. **Code Reviews**: All changes require review before merging
5. **Testing**: Include security testing in your test suite

### For Users

When integrating Proteus into your application:

1. **Debug UI**: Disable override UI in production builds

2. **Network Security**: Use HTTPS for all remote configuration endpoints

3. **Data Protection**: Be cautious with sensitive configuration values
   - Avoid storing sensitive data in remote configurations
   - Use proper encryption for sensitive local storage

4. **Version Updates**: Keep Proteus updated to the latest stable version

5. **Permissions**: Follow the principle of least privilege

### Security Features

Proteus includes several built-in security features:

- **Type Safety**: Compile-time type checking prevents configuration errors
- **Local Override Protection**: Override UI should only be available in debug builds
- **Input Validation**: All configuration values are validated for type safety
- **Secure Storage**: SharedPreferences storage follows Android security best practices

## Known Security Considerations

### Override UI in Production

⚠️ **Critical**: The override UI (`proteus-ui` module) should never be enabled in production builds.

```kotlin
// ✅ Correct - Only in debug
if (BuildConfig.DEBUG) {
    startActivity(Intent(this, FeatureBookActivity::class.java))
}

// ❌ Incorrect - Available in all builds
startActivity(Intent(this, FeatureBookActivity::class.java))
```

### Configuration Data Sensitivity

- Remote configuration values are cached locally in SharedPreferences
- Do not store passwords, API keys, or other sensitive data in configurations
- Consider the security implications of configuration changes in production

### Provider Security

Different providers have different security characteristics:

- **Firebase Remote Config**: Follows Firebase security model
- **Custom Providers**: Security depends on your implementation
- **Mock Provider**: Local-only, but accessible via debug UI

## Security Updates

Security updates will be released as patch versions (e.g., 1.0.1, 1.1.3) and will be clearly marked in the changelog with a `[SECURITY]` tag.

Subscribe to releases on GitHub to be notified of security updates:
1. Go to the [Proteus repository](https://github.com/maxim-petlyuk/proteus)
2. Click "Watch" → "Custom" → "Releases"

## Acknowledgments

We appreciate the security research community and will acknowledge researchers who responsibly disclose security vulnerabilities:

- Security researchers who report vulnerabilities will be credited in the security advisory (unless they prefer to remain anonymous)
- We maintain a hall of fame for security contributors

## Contact Information

For general security questions or concerns:
- **Email**: mpetlyuk@gmail.com
- **GitHub**: Open a private security advisory via GitHub's security tab

For general support and non-security issues:
- **GitHub Issues**: [Create an issue](https://github.com/maxim-petlyuk/proteus/issues)
- **GitHub Discussions**: [Join discussions](https://github.com/maxim-petlyuk/proteus/discussions)

## Legal

This security policy is subject to our [Code of Conduct](CODE_OF_CONDUCT.md) and [Contributing Guidelines](CONTRIBUTING.md).

---

*Last updated: December 2024*
*Version: 1.0*