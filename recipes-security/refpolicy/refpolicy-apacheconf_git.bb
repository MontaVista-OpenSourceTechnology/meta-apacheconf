require ../../../meta-selinux/recipes-security/refpolicy/refpolicy-minimum_${PV}.bb

SUMMARY = "SELinux apacheconf policy"
DESCRIPTION = "\
This is a reference policy for apache \
"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=393a5ca445f6965873eca0259a17f833"

SRC_URI += " \
	file://0001-Add-super-secret-module.patch \
	"

FILESEXTRAPATHS:prepend := "${THISDIR}/../../../meta-selinux/recipes-security/refpolicy/refpolicy:"
FILESEXTRAPATHS:prepend := "${THISDIR}/../../../meta-selinux/recipes-security/refpolicy/files:"
FILESEXTRAPATHS:prepend := "${THISDIR}/refpolicy:"

POLICY_NAME = "apacheconf"

EXTRA_POLICY_MODULES += "apache"
EXTRA_POLICY_MODULES += "supsec"
