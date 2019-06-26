package hbs.com.snackb.ui

import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.ux.ArFragment

class CloudAnchorFragment : ArFragment() {
    override fun getSessionConfiguration(session: Session?): Config {
        //planeDiscoveryController.setInstructionView(null)
        return super.getSessionConfiguration(session).apply {
            cloudAnchorMode = Config.CloudAnchorMode.ENABLED
        }
    }
}