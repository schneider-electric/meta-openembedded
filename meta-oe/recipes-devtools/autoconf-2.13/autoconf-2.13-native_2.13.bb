SUMMARY = "A GNU tool that produce shell scripts to automatically configure software"
DESCRIPTION = "Autoconf is an extensible package of M4 macros that produce shell scripts to automatically \ 
configure software source code packages. Autoconf creates a configuration script for a package from a template \
file that lists the operating system features that the package can use, in the form of M4 macro calls."
SECTION = "devel"

HOMEPAGE = "http://www.gnu.org/software/autoconf/"

LICENSE = "GPL-3.0-only"
LICENSE = "GPL-2.0-only & GPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=361b6b837cad26c6900a926b62aada5f"

SRC_URI = " \
    ${GNU_MIRROR}/autoconf/autoconf-${PV}.tar.gz \
    file://0001-Add-config.guess-config.sub-install-to-destdir.patch \
"

S = "${WORKDIR}/${BPN}"
SRC_URI[sha256sum] = "f0611136bee505811e9ca11ca7ac188ef5323a8e2ef19cffd3edb3cf08fd791e"

inherit texinfo native

DEPENDS += "m4-native gnu-config-native"
RDEPENDS:${PN} = "m4-native gnu-config-native"

PERL = "${USRBINPATH}/perl"
PERL:class-native = "/usr/bin/env perl"

CACHED_CONFIGUREVARS += "ac_cv_path_PERL='${PERL}' \
                         ac_cv_path_M4=m4 \
                        "

CONFIGUREOPTS = " \
    --build=${BUILD_SYS} \
    --host=${HOST_SYS} \
    --target=${TARGET_SYS} \
    --prefix=${prefix} \
    --exec_prefix=${exec_prefix} \
    --bindir=${bindir} \
    --sbindir=${sbindir} \
    --libexecdir=${libexecdir} \
    --datadir=${datadir} \
    --sysconfdir=${sysconfdir} \
    --sharedstatedir=${sharedstatedir} \
    --localstatedir=${localstatedir} \
    --libdir=${libdir} \
    --includedir=${includedir} \
    --oldincludedir=${includedir} \
    --infodir=${infodir} \
    --mandir=${mandir} \
    --disable-silent-rules \
"

do_configure() {
    ${CACHED_CONFIGUREVARS} ./configure ${CONFIGUREOPTS}
}

do_install() {
	oe_runmake 'DESTDIR=${D}' install

    # avoid conflicts with standard autotools
    ver="213"
    for file in `find ${D}${bindir} -type f`; do
        mv $file $file$ver
    done
	mv ${D}${datadir}/autoconf ${D}${datadir}/autoconf213
}
