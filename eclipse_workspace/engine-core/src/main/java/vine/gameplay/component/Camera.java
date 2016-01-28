package vine.gameplay.component;

/**
 * @author Steffen
 *
 */
public class Camera extends Component {

    @Override
    public void onDestroy() {
        this.entity.getScene().cameras.removeCamera(this);
    }
}