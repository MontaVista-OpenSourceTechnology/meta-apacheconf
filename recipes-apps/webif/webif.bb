DESCRIPTION = "Dummy web interface"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = "\
	file://webif \
	"

RDEPENDS:${PN} += "bash"

FILES:${PN} += "${libexecdir}/apache2/modules/cgi-bin/webif"

do_compile() {
}

do_install() {
        install -d ${D}${libexecdir}/apache2/modules/cgi-bin
        install -m 0755 ${WORKDIR}/webif ${D}${libexecdir}/apache2/modules/cgi-bin
}

pkg_postinst_ontarget:${PN} () {
    # Apache2 might by default turn off CGI
    sed -e 's/^#LoadModule cgid_module/LoadModule cgid_module/g' -i ${sysconfdir}/apache2/httpd.conf
}
