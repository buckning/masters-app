package occupationalTherapies.activities.game;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.ContactListener;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.JointType;
import org.jbox2d.dynamics.joints.MouseJoint;
import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import advanced.physics.physicsShapes.PhysicsCircle;
import advanced.physics.physicsShapes.PhysicsRectangle;
import advanced.physics.util.PhysicsHelper;

import occupationalTherapies.activities.GenericActivity;
import occupationaltherapy.mainMenu.MainMenuScene;
import processing.core.PImage;

public class GameActivity extends GenericActivity{

	private float scale = 20;
	private float timeStep = 1.0f/30.0f;
	private World world;
	private static final int IDLE_WORLD_BODY_COUNT = 7;
	private static final int NUMBER_OF_BLOCKS = 6;
	private static final int BLOCK_Y_POSITION = 80;
	private UpdateGameActivity updater;
	private MTComponent physicsContainer;
	private PhysicsCircle ball, paddle;
	private PhysicsRectangle[] screenBorders;
	
	public GameActivity(MTApplication mtapp) {
		super(mtapp, "Game Activity", MainMenuScene.getSessionManager());
		useGradientBackground();
	}

	@Override
	public void load() {
		MTApplication mtapp = getMTApplication();
		float worldOffset = 10;
		AABB worldAABB = new AABB(new Vec2(-worldOffset, -worldOffset), new Vec2(mtapp.width/scale+worldOffset, mtapp.height/scale + worldOffset));
		Vec2 gravity = new Vec2(0, 3f);
		boolean sleep = true;
		this.world = new World(worldAABB, gravity, sleep);
		
		updater = new UpdateGameActivity(world, timeStep, 10, scale);
	
		physicsContainer = new MTComponent(mtapp);
		physicsContainer.scale(scale, scale, 1, Vector3D.ZERO_VECTOR);
		addChild(physicsContainer);
		
		createScreenBorders(physicsContainer);
		
		ball = new PhysicsCircle(mtapp, new Vector3D(350, 400), 50, world, 1.0f, 0.3f, 0.7f, scale);
		ball.setTexture(mtapp.loadImage("ball.png"));
		ball.setFillColor(MTColor.LIME);
		PhysicsHelper.addDragJoint(world, ball, ball.getBody().isDynamic(), scale);
		physicsContainer.addChild(ball);
		
		for(int i = 0; i < NUMBER_OF_BLOCKS; i++){
			PhysicsRectangle rect = new PhysicsRectangle(new Vector3D(70 + 130*i, BLOCK_Y_POSITION), 100, 50, getMTApplication(), world, 0f, 0.4f, 0.4f, scale);
			rect.setFillColor(MTColor.FUCHSIA);
			physicsContainer.addChild(rect);
		}
		
		float paddleRad = 10;
		paddle = new PhysicsCircle(mtapp, new Vector3D(80, mtapp.height/2f), paddleRad, world, 1.0f, 0.3f, 0.4f, scale);
		paddle.setNoFill(true);
		paddle.setNoStroke(true);
		paddle.setPickable(false);
		
		addContactListener();
		MTRectangle rect = new MTRectangle(
				PhysicsHelper.scaleDown(0, scale), PhysicsHelper.scaleDown(0, scale), 
				PhysicsHelper.scaleDown(mtapp.width, scale), PhysicsHelper.scaleDown(mtapp.height, scale), mtapp);
		
		rect.setNoFill(true);
		rect.setNoStroke(true);
		rect.unregisterAllInputProcessors();
		rect.removeAllGestureEventListeners(DragProcessor.class);
		rect.registerInputProcessor(new DragProcessor(mtapp));
		rect.addGestureListener(DragProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				DragEvent de = (DragEvent)ge;
				interactionOccured();
				
				try{
					Body body = (Body)paddle.getUserData("box2d");
					MouseJoint mouseJoint;
					Vector3D to = new Vector3D(de.getTo());
					//Un-scale position from mt4j to box2d
					PhysicsHelper.scaleDown(to, scale);
					switch (de.getId()) {
					case DragEvent.GESTURE_DETECTED:
						paddle.setVisible(true);
						paddle.sendToFront();
						body.wakeUp();
						body.setXForm(new Vec2(to.x,  to.y), body.getAngle());
						mouseJoint = PhysicsHelper.createDragJoint(world, body, to.x, to.y);
						paddle.setUserData(paddle.getID(), mouseJoint);
						break;
					case DragEvent.GESTURE_UPDATED:
						mouseJoint = (MouseJoint) paddle.getUserData(paddle.getID());
						if (mouseJoint != null){
							mouseJoint.setTarget(new Vec2(to.x, to.y));	
							int jx =(int)(to.x*scale);
							int jy = (int)(to.y*scale);
							logInteraction(jx, jy, "i");
						}
						break;
					case DragEvent.GESTURE_ENDED:
						mouseJoint = (MouseJoint) paddle.getUserData(paddle.getID());
//					TODO	paddle.setVisible(false);
						
						if (mouseJoint != null){
							paddle.setUserData(paddle.getID(), null);
							//Only destroy the joint if it isnt already (go through joint list and check)
							for (Joint joint = world.getJointList(); joint != null; joint = joint.getNext()) {
								JointType type = joint.getType();
								switch (type) {
								case MOUSE_JOINT:
									MouseJoint mj = (MouseJoint)joint;
									if (body.equals(mj.getBody1()) || body.equals(mj.getBody2())){
										if (mj.equals(mouseJoint)) {
											world.destroyJoint(mj);
										}
									}
									break;
								default:
									break;
								}
							}
						}
						mouseJoint = null;
						break;
					default:
						break;
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				return false;
			}
		});
		physicsContainer.addChild(0,rect);
		homeButton.sendToFront();
		infoButton.sendToFront();
		MTRectangle info = new MTRectangle(0, 0, 400, 300, mtapp);
		IFont font = FontManager.getInstance().createFont(mtapp,"arial.ttf", 30,  MTColor.WHITE, MTColor.WHITE);
		MTTextArea infoArea = new MTTextArea(0, 300, 400, 300, font, mtapp);
		infoArea.setText("Touch and drag the ball\nThe goal is to hit the \nblocks with the ball");
		infoArea.setNoFill(true);
		infoArea.setNoStroke(true);
		PImage texture = mtapp.loadImage(imagePath +"game.png");
		texture.resize(400, 300);
		MTImage image = new MTImage(texture, mtapp);
		info.addChild(image);
		info.addChild(infoArea);
		setInformation(info);
	}
	
	private void addContactListener(){
		world.setContactListener(new ContactListener() {
			
			@Override
			public void result(ContactResult arg0) {/*not interested*/}
			
			@Override
			public void remove(ContactPoint cp) {
				
			}
			
			@Override
			public void persist(ContactPoint arg0) {/*not interested*/}
			
			@Override
			public void add(ContactPoint cp) {/*not interested*/
				
				Shape shape1 = cp.shape1;
				Shape shape2 = cp.shape2;
				Body body1 = shape1.getBody();
				Body body2 = shape2.getBody();
				Object object1 = body1.getUserData();
				Object object2 = body2.getUserData();
				
				if(object1 instanceof PhysicsRectangle || object2 instanceof PhysicsRectangle){
					PhysicsCircle circle = null;
					int id = -1;
					
					if(object1 instanceof PhysicsCircle){
						id = 1;
						circle = (PhysicsCircle)object1;
					}
					else if(object2 instanceof PhysicsCircle){
						id = 2;
						circle = (PhysicsCircle)object2;
					}
					
					if(circle != null){
						if(circle.equals(paddle))return;
						PhysicsRectangle rectangle;
						if(id == 1)rectangle = (PhysicsRectangle)object2;
						else rectangle = (PhysicsRectangle)object1;
						
						//count the screen borders, if the ball hit any of the borders,
						//var will be less than 4
						int var = 0;
						for(int i = 0; i < screenBorders.length; i++){
							if(rectangle.equals(screenBorders[i])){
								break;
							}
							var++;
						}
						if(var == 4){
							//if var == 4, a collision with a block has occurred
						
							//unexpected crash occurs here.	(this was fixed by putting it in the add method
							//  of the contact listener. It crashes when in the remove() method
							updater.destroyBody(rectangle.getBody());
							rectangle.removeAllChildren();
							rectangle.destroy();
							rectangle = null;
						}
					}
				}
				
			}
		});
	}
	
	public void createScreenBorders(MTComponent parent){
		MTApplication mtapp = getMTApplication();
		screenBorders = new PhysicsRectangle[4];
		float borderWidth = 50f;
		float borderHeight = mtapp.height;
		
		Vector3D pos = new Vector3D(-(borderWidth/2f), mtapp.height/2f);
		PhysicsRectangle borderLeft = new PhysicsRectangle(pos, borderWidth, borderHeight, mtapp, world, 0, 0, 0, scale);
		borderLeft.setName("borderLeft");
		parent.addChild(borderLeft);
		screenBorders[0] = borderLeft;
		
		pos = new Vector3D(mtapp.width+(borderWidth/2f), mtapp.height/2f);
		PhysicsRectangle borderRight = new PhysicsRectangle(pos, borderWidth, borderHeight, mtapp, world, 0, 0, 0, scale);
		borderRight.setName("borderRight");
		parent.addChild(borderRight);
		screenBorders[1] = borderRight;
		
		borderWidth = mtapp.width;
		borderHeight = 50.0f;
		pos = new Vector3D(mtapp.width/2, -(borderHeight/2));
		PhysicsRectangle borderTop = new PhysicsRectangle(pos, borderWidth, borderHeight, mtapp, world, 0, 0, 0, scale);
		borderTop.setName("borderTop");
		parent.addChild(borderTop);
		screenBorders[2] = borderTop;
		
		pos = new Vector3D(mtapp.width/2, mtapp.height+(borderHeight/2));
		PhysicsRectangle borderBottom = new PhysicsRectangle(pos, borderWidth, borderHeight, mtapp, world, 0, 0, 0, scale);
		borderBottom.setName("borderBottom");
		parent.addChild(borderBottom);
		screenBorders[3] = borderBottom;
	}
	
	@Override
	public void preDrawAction(){
		updater.processAction();
		
		if(world.getBodyCount() <= IDLE_WORLD_BODY_COUNT){
		//	System.out.println("???"+world.getBodyCount());
			if(ball.getCenterPointGlobal().y > 400){
				getMTApplication().invokeLater(new Runnable() {
					
					@Override
					public void run() {
						logInteraction(0, 0, "Game_Complete");
						for(int i = 0; i < NUMBER_OF_BLOCKS; i++){
							PhysicsRectangle rect = new PhysicsRectangle(new Vector3D(70 + 130*i, BLOCK_Y_POSITION), 100, 50, getMTApplication(), world, 0f, 0.4f, 0.4f, scale);
							rect.setFillColor(MTColor.FUCHSIA);
							physicsContainer.addChild(rect);
						}
					}
				});
			}
		}
	}
}
