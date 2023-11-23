# RexCraft 1.19.2 Overview 
Much of the work I've done with this mod leverages inheritance, and therefore uses a lot of existing code, though this is not entirely the case. This mod adds in mobs, materials, tools, and armor, however is more focused on the mobs.

<img src="https://github.com/JasaurusRex/RexCraft1.19.2/assets/106399510/2b911d98-037c-4d2d-adea-cb7918bc1052" width=30% height=50%>

# The main goal 
My main goal with this project is to add dinosaurs to Minecraft, specifically in a way that's more than just adding in a glorified dog. I want to add some more complex behaviors to these entities, making them more interesting to tame and interact with.    

# The Process
Through the use of a lot of YouTube videos and reading documentation, I've become pretty familiar with Minecraft's code and the Forge modding tools. I started with smaller, more simple entities; those being the hedgehog and leopard gecko. I used these two to get familiar with the tools and become comfortable with the development environment. Through tinkering with these entities I learned a lot about what I was able to do with inheritance, as well as other libraries such as Geckolib. I've become particularly fond of Geckolib and  Blockbench, as they have streamlined the modeling, texturing, and animating of the entities, so much so that I've learned I also enjoy that aspect of this development process. I also got to test smaller features with these entities, specifically being able to modify the texture, model, or animation of an entity by changing its name in-game. This actually allowed me to make it so that when I name a gecko "Nova" its texture changes to match the pattern of my pet in real life.

<img src="https://github.com/JasaurusRex/RexCraft1.19.2/assets/106399510/4d125d86-d1fa-473d-88ce-b4024e774c86" width=50% height=50%>    <img src="https://github.com/JasaurusRex/RexCraft1.19.2/assets/106399510/b9347f9b-488f-461a-a7af-ca0cd1958def" width=40% height=50%>

# The Dinosaurs
As mentioned before, my goal here is to not just add dinosaurs but to make them more interesting than just a mount or a tame. I want them to be unique, but not too complex as this is a Minecraft mod (I also want them to be balanced of course). As of right now, my plan is to have different types of tames with the dinosaurs. Some will be more useful for combat, others for transportation, and some for utility. The two dinosaurs currently in the mod show this: The Megalosaurus being more of a combat-focused mount, while the Iguanodon is better for transporting resources and defense. I also want to make their behavior more complex. I've achieved this so far by giving the current dinosaurs an "angry" mode where they are enraged, becoming faster, and sometimes doing more damage. I'd like to make more complex behaviors in the future like pack hunting and potentially herd-like behaviors. A lot of this is done by messing with the "goals" of the entities in the respective class. Most of what I've accomplished is done through leveraging inheritance and object-oriented programming, which makes use of a lot of code already in the project.

<img src="https://github.com/JasaurusRex/RexCraft1.19.2/assets/106399510/77d54d2b-5d8c-46a3-8846-6677fc8e9fd5" width=50% height=50%>

# Other Features
The materials, tools, and armor are all the same story. A good example of this is that I built the "bleed' status effect off of poison, but modified it to do what I wanted. I could then add this effect to certain weapons to make them more dynamic. I've had a lot of fun adding in different advancements, and slipping in references to the media that inspired a lot of the content in this mod. 

<img src="https://github.com/JasaurusRex/RexCraft1.19.2/assets/106399510/915b8a09-8ba8-40a8-a9ef-8afe0e01fe5e" width=15% height=50%>
<img src="https://github.com/JasaurusRex/RexCraft1.19.2/assets/106399510/d7793291-7b53-4463-8741-9c3e0420eb45" width=30% height=50%>


