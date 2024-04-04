MultiplatformModsDotGroovy.make {
    modLoader = 'javafml'
    loaderVersion = '[1,)'
    issueTrackerUrl = 'https://github.com/lukebemishprojects/RevampedPhantoms/issues'
    license = 'LGPL-3.0-or-later'

    mod {
        modId = buildProperties.mod_id
        displayName = buildProperties.mod_name
        version = environmentInfo.version
        displayUrl = 'https://github.com/lukebemishprojects/RevampedPhantoms'
        contact {
            sources = 'https://github.com/lukebemishprojects/RevampedPhantoms'
        }
        author = 'Luke Bemish'
        description = buildProperties.mod_description

        entrypoints {
            main = 'dev.lukebemish.revampedphantoms.fabric.ModEntrypoint'
        }

        dependencies {
            minecraft = ">= ${libs.versions.minecraft}"
            onNeoForge {
                neoforge = ">=${libs.versions.neoforge}"
            }
            onFabric {
                mod 'fabricloader', {
                    versionRange = ">=${libs.versions.fabric_loader}"
                }
                mod 'fabric-api', {
                    versionRange = ">=${libs.versions.fabric_api.split(/\+/)[0]}"
                }
            }
        }
    }
    mixins {
        mixin('mixin.revamped_phantoms.json')
    }
}
