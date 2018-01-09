package occupationalTherapies.activities.game;

import java.util.ArrayList;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import advanced.physics.util.UpdatePhysicsAction;

public class UpdateGameActivity extends UpdatePhysicsAction{
	
	private ArrayList<Body> bodies;
	private World world;
	
	public UpdateGameActivity(World world, float timeStep, int constraintIterations, float scale){
		super(world, timeStep, constraintIterations, scale);
		this.world = world;
		bodies = new ArrayList<Body>();
	}
	@Override
	public void processAction(){
		synchronized(this){
			super.processAction();
		
			//loop through all the bodies that need to be destroyed  and remove them
			for(Body b: bodies){
				world.destroyBody(b);
			}
			//remove all destroyed bodies
			for(int i = 0; i < bodies.size(); i++){
				bodies.remove(i);
			}
		}
	}
	
	public void destroyBody(Body b){
		bodies.add(b);
	}
}
