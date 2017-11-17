import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Class representing individual woman's socio-economic and demographic characteristics as shown below:
 * 
 * 1. Wife's age                     (numerical)
 * 2. Wife's education               (categorical)      1=low, 2, 3, 4=high
 * 3. Husband's education            (categorical)      1=low, 2, 3, 4=high
 * 4. Number of children ever born   (numerical)
 * 5. Wife's religion                (binary)           0=Non-Islam, 1=Islam
 * 6. Wife's now working?            (binary)           0=Yes, 1=No
 * 7. Husband's occupation           (categorical)      1, 2, 3, 4
 * 8. Standard-of-living index       (categorical)      1=low, 2, 3, 4=high
 * 9. Media exposure                 (binary)           0=Good, 1=Not good
 * 10. Contraceptive method used     (class attribute)  1=No-use, 2=Long-term, 3=Short-term
 * obtained from
 * 		http://archive.ics.uci.edu/ml/datasets/Contraceptive+Method+Choice
 * @author Scott Stuart
*/
public class Woman {
	public int age;
	public int education;
	public int husbandEducation;
	public int children;
	public int religion;
	public int working;
	public int husbandJob;
	public int livingStandard;
	public int media;
	public int contraceptive;
	public ArrayList<Integer> info;
	
	public Woman(int age, int education, int husbandEducation,  int children, int religion,  int isWorking, int husbandJob, int living, int media) {
		this.age = age;
		this.education = education;
		this.husbandEducation = husbandEducation;
		this.children = children;
		this.religion = religion;
		this.working = isWorking;
		this.husbandJob = husbandJob;
		this.livingStandard = living;
		this.media = media;
		this.info = new ArrayList<Integer>();
		this.info.addAll(Arrays.asList(age, education, husbandEducation, children, religion, isWorking, husbandJob, living, media));
	}
	
	
	public Woman(int age, int education, int husbandEducation,  int children, int religion,  int isWorking, int husbandJob, int living, int media, int contraceptive) {
		this.age = age;
		this.education = education;
		this.husbandEducation = husbandEducation;
		this.children = children;
		this.religion = religion;
		this.working = isWorking;
		this.husbandJob = husbandJob;
		this.livingStandard = living;
		this.media = media;
		this.contraceptive = contraceptive;
		this.info = new ArrayList<Integer>();
		this.info.addAll(Arrays.asList(age, education, husbandEducation, children, religion, isWorking, husbandJob, living, media, contraceptive));
	}
	
	public int getAge() {
		return age;
	}
	
	public int getEducation() {
		return education;
	}
	
	public int getHusbandEducation() {
		return husbandEducation;
	}
	
	public int getChildren() {
		return children;
	}
	
	public int getReligion() {
		return religion;
	}
	
	public int isWorking() {
		return working;
	}
	
	public int getHusbandJob() {
		return husbandJob;
	}
	
	public int getLivingStandard() {
		return livingStandard;
	}
	
	public int getMediaExposure() {
		return media;
	}
	
	public int getContraceptiveMethod() {
		return contraceptive;
	}
	
	public List<Integer> getInfo() {
		return info;
	}
	
	public String toString() {
		String str = ("[Age: " + getAge() + ", Education: " + getEducation() + ", Husband's Education: " + getHusbandEducation() + ", Children: " + getChildren() + ", Relgious: " + getReligion() + ", Working: " + isWorking() + ", Husband's Job: " + getHusbandJob());
		str+= ", Living Standard: " + getLivingStandard() + ", Media Exposure: " + getMediaExposure() + ", Contraceptive Method: " + getContraceptiveMethod() + "]";
		//String str = (getAge() + "," + getEducation() + "," + getHusbandEducation() + "," + getChildren() + "," + getReligion() + "," + isWorking() + "," + getHusbandJob()+ "," + getLivingStandard() + "," + getMediaExposure() + "," + getContraceptiveMethod());
		return str;
	}
	
}
