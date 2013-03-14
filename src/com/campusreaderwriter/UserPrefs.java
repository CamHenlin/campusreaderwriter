package com.campusreaderwriter;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceException;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.User;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.BitSet;
import java.util.logging.Logger;
import javax.jdo.JDODetachedFieldAccessException;
import javax.jdo.JDOFatalInternalException;
import javax.jdo.PersistenceManager;
import javax.jdo.identity.StringIdentity;
import javax.jdo.spi.Detachable;
import javax.jdo.spi.JDOImplHelper;
import javax.jdo.spi.PersistenceCapable;
import javax.jdo.spi.PersistenceCapable.ObjectIdFieldConsumer;
import javax.jdo.spi.PersistenceCapable.ObjectIdFieldSupplier;
import javax.jdo.spi.StateManager;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity(name="UserPrefs")
public class UserPrefs
  implements Serializable, Detachable, PersistenceCapable
{

  @Transient
  private static Logger logger = Logger.getLogger(UserPrefs.class.getName());

  @Id
  private String userId;
  private int tzOffset;
  private String textInput;
  private String emailInput;

  @Basic
  private User user;
  protected transient StateManager jdoStateManager;
  protected transient byte jdoFlags;
  protected Object[] jdoDetachedState;
  private static final byte[] jdoFieldFlags;
  private static final Class jdoPersistenceCapableSuperclass;
  private static final Class[] jdoFieldTypes;
  private static final String[] jdoFieldNames = __jdoFieldNamesInit();
  private static final int jdoInheritedFieldCount;
  private static final long serialVersionUID = -7618976250820543714L;

  static { jdoFieldTypes = __jdoFieldTypesInit(); jdoFieldFlags = __jdoFieldFlagsInit(); jdoInheritedFieldCount = __jdoGetInheritedFieldCount(); jdoPersistenceCapableSuperclass = __jdoPersistenceCapableSuperclassInit(); JDOImplHelper.registerClass(___jdo$loadClass("com.campusreaderwriter.UserPrefs"), jdoFieldNames, jdoFieldTypes, jdoFieldFlags, jdoPersistenceCapableSuperclass, new UserPrefs());
  }

  public UserPrefs(User user)
  {
    this.userId = user.getUserId();
    this.user = user;
  }

  public String getUserId() {
    return jdoGetuserId(this);
  }

  public int getTzOffset() {
    return jdoGettzOffset(this);
  }

  public void setTzOffset(int tzOffset) {
    jdoSettzOffset(this, tzOffset);
  }

  public void setTextInput(String textInput) {
    jdoSettextInput(this, textInput);
  }

  public void setEmailInput(String emailInput) {
    jdoSetemailInput(this, emailInput);
  }

  public String getTextInput() {
    return jdoGettextInput(this);
  }

  public String getEmailInput() {
    return jdoGetemailInput(this);
  }

  public User getUser() {
    return jdoGetuser(this);
  }

  public void setUser(User user) {
    jdoSetuser(this, user);
  }

  public static UserPrefs getPrefsForUser(User user) {
    UserPrefs userPrefs = null;

    String cacheKey = getCacheKeyForUser(user);
    try
    {
      MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
      if (memcache.contains(cacheKey)) {
        logger.warning("CACHE HIT: " + cacheKey);
        return (UserPrefs)memcache.get(cacheKey);
      }

      logger.warning("CACHE MISS: " + cacheKey);
    }
    catch (MemcacheServiceException localMemcacheServiceException)
    {
    }

    EntityManager em = EMF.get().createEntityManager();
    try {
      userPrefs = (UserPrefs)em.find(UserPrefs.class, user.getUserId());
      if (userPrefs == null)
        userPrefs = new UserPrefs(user);
      else
        userPrefs.cacheSet();
    }
    finally {
      em.close();
    }

    return userPrefs;
  }

  public static String getCacheKeyForUser(User user) {
    return "UserPrefs:" + user.getUserId();
  }

  public String getCacheKey() {
    return getCacheKeyForUser(getUser());
  }

  public void save() {
    EntityManager em = EMF.get().createEntityManager();
    try {
      em.merge(this);
      cacheSet();
    } finally {
      em.close();
    }
  }

  public void cacheSet() {
    try {
      MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
      memcache.put(getCacheKey(), this);
    }
    catch (MemcacheServiceException localMemcacheServiceException)
    {
    }
  }

  protected UserPrefs()
  {
  }

  public void jdoCopyKeyFieldsFromObjectId(PersistenceCapable.ObjectIdFieldConsumer fc, Object oid)
  {
    if (fc == null)
      throw new IllegalArgumentException("ObjectIdFieldConsumer is null");
    if (!(oid instanceof StringIdentity))
      throw new ClassCastException("oid is not instanceof javax.jdo.identity.StringIdentity");
    StringIdentity o = (StringIdentity)oid;
    fc.storeStringField(4, o.getKey());
  }

  protected void jdoCopyKeyFieldsFromObjectId(Object oid)
  {
    if (!(oid instanceof StringIdentity))
      throw new ClassCastException("key class is not javax.jdo.identity.StringIdentity or null");
    StringIdentity o = (StringIdentity)oid;
    this.userId = o.getKey();
  }

  public void jdoCopyKeyFieldsToObjectId(Object oid)
  {
    throw new JDOFatalInternalException("It's illegal to call jdoCopyKeyFieldsToObjectId for a class with SingleFieldIdentity.");
  }

  public void jdoCopyKeyFieldsToObjectId(PersistenceCapable.ObjectIdFieldSupplier fs, Object paramObject)
  {
    throw new JDOFatalInternalException("It's illegal to call jdoCopyKeyFieldsToObjectId for a class with SingleFieldIdentity.");
  }

  public final Object jdoGetObjectId()
  {
    if (this.jdoStateManager != null)
      return this.jdoStateManager.getObjectId(this);
    if (!jdoIsDetached())
      return null;
    return this.jdoDetachedState[0];
  }

  public final Object jdoGetVersion()
  {
    if (this.jdoStateManager != null)
      return this.jdoStateManager.getVersion(this);
    if (!jdoIsDetached())
      return null;
    return this.jdoDetachedState[1];
  }

  protected final void jdoPreSerialize()
  {
    if (this.jdoStateManager != null)
      this.jdoStateManager.preSerialize(this);
  }

  public final PersistenceManager jdoGetPersistenceManager()
  {
    return this.jdoStateManager != null ? this.jdoStateManager.getPersistenceManager(this) : null;
  }

  public final Object jdoGetTransactionalObjectId()
  {
    return this.jdoStateManager != null ? this.jdoStateManager.getTransactionalObjectId(this) : null;
  }

  public final boolean jdoIsDeleted()
  {
    return this.jdoStateManager != null ? this.jdoStateManager.isDeleted(this) : false;
  }

  public final boolean jdoIsDirty()
  {
    if (this.jdoStateManager != null)
      return this.jdoStateManager.isDirty(this);
    if (!jdoIsDetached())
      return false;
    return ((BitSet)this.jdoDetachedState[3]).length() > 0;
  }

  public final boolean jdoIsNew()
  {
    return this.jdoStateManager != null ? this.jdoStateManager.isNew(this) : false;
  }

  public final boolean jdoIsPersistent()
  {
    return this.jdoStateManager != null ? this.jdoStateManager.isPersistent(this) : false;
  }

  public final boolean jdoIsTransactional()
  {
    return this.jdoStateManager != null ? this.jdoStateManager.isTransactional(this) : false;
  }

  public void jdoMakeDirty(String fieldName)
  {
    if (this.jdoStateManager != null)
      this.jdoStateManager.makeDirty(this, fieldName);
    if ((jdoIsDetached()) && (fieldName != null))
    {
      String fldName = null;
      if (fieldName.indexOf('.') >= 0)
        fldName = fieldName.substring(fieldName.lastIndexOf('.') + 1);
      else
        fldName = fieldName;
      for (int i = 0; i < jdoFieldNames.length; i++)
        if (jdoFieldNames[i].equals(fldName))
        {
          if (((BitSet)this.jdoDetachedState[2]).get(i + jdoInheritedFieldCount))
          {
            ((BitSet)this.jdoDetachedState[3]).set(i + jdoInheritedFieldCount);
            return;
          }
          throw new JDODetachedFieldAccessException("You have just attempted to access a field/property that hasn't been detached. Please detach it first before performing this operation");
        }
    }
  }

  public Object jdoNewObjectIdInstance()
  {
    return new StringIdentity(getClass(), this.userId);
  }

  public Object jdoNewObjectIdInstance(Object key)
  {
    if (key == null)
      throw new IllegalArgumentException("key is null");
    if (!(key instanceof String))
      return new StringIdentity(getClass(), (String)key);
    return new StringIdentity(getClass(), (String)key);
  }

  public final void jdoProvideFields(int[] indices)
  {
    if (indices == null)
      throw new IllegalArgumentException("argment is null");
    int i = indices.length - 1;
    if (i >= 0)
      do
      {
        jdoProvideField(indices[i]);
        i--;
      }
      while (i >= 0);
  }

  public final void jdoReplaceFields(int[] indices)
  {
    if (indices == null)
      throw new IllegalArgumentException("argument is null");
    int i = indices.length;
    if (i > 0)
    {
      int j = 0;
      do
      {
        jdoReplaceField(indices[j]);
        j++;
      }
      while (j < i);
    }
  }

  public final void jdoReplaceFlags()
  {
    if (this.jdoStateManager != null)
      this.jdoFlags = this.jdoStateManager.replacingFlags(this);
  }

  public final synchronized void jdoReplaceStateManager(StateManager sm)
  {
    if (this.jdoStateManager != null)
    {
      this.jdoStateManager = this.jdoStateManager.replacingStateManager(this, sm);
    }
    else
    {
      JDOImplHelper.checkAuthorizedStateManager(sm);
      this.jdoStateManager = sm;
      this.jdoFlags = 1;
    }
  }

  public final synchronized void jdoReplaceDetachedState()
  {
    if (this.jdoStateManager == null)
      throw new IllegalStateException("state manager is null");
    this.jdoDetachedState = this.jdoStateManager.replacingDetachedState(this, this.jdoDetachedState);
  }

  public boolean jdoIsDetached()
  {
    return (this.jdoStateManager == null) && (this.jdoDetachedState != null);
  }

  public PersistenceCapable jdoNewInstance(StateManager sm)
  {
    UserPrefs result = new UserPrefs();
    result.jdoFlags = 1;
    result.jdoStateManager = sm;
    return result;
  }

  public PersistenceCapable jdoNewInstance(StateManager sm, Object obj)
  {
    UserPrefs result = new UserPrefs();
    result.jdoFlags = 1;
    result.jdoStateManager = sm;
    result.jdoCopyKeyFieldsFromObjectId(obj);
    return result;
  }

  public void jdoReplaceField(int index)
  {
    if (this.jdoStateManager == null)
      throw new IllegalStateException("state manager is null");
    switch (index)
    {
    case 0:
      this.emailInput = this.jdoStateManager.replacingStringField(this, index);
      break;
    case 1:
      this.textInput = this.jdoStateManager.replacingStringField(this, index);
      break;
    case 2:
      this.tzOffset = this.jdoStateManager.replacingIntField(this, index);
      break;
    case 3:
      this.user = ((User)this.jdoStateManager.replacingObjectField(this, index));
      break;
    case 4:
      this.userId = this.jdoStateManager.replacingStringField(this, index);
      break;
    default:
      throw new IllegalArgumentException("out of field index :" + index);
    }
  }

  public void jdoProvideField(int index)
  {
    if (this.jdoStateManager == null)
      throw new IllegalStateException("state manager is null");
    switch (index)
    {
    case 0:
      this.jdoStateManager.providedStringField(this, index, this.emailInput);
      break;
    case 1:
      this.jdoStateManager.providedStringField(this, index, this.textInput);
      break;
    case 2:
      this.jdoStateManager.providedIntField(this, index, this.tzOffset);
      break;
    case 3:
      this.jdoStateManager.providedObjectField(this, index, this.user);
      break;
    case 4:
      this.jdoStateManager.providedStringField(this, index, this.userId);
      break;
    default:
      throw new IllegalArgumentException("out of field index :" + index);
    }
  }

  protected final void jdoCopyField(UserPrefs obj, int index)
  {
    switch (index)
    {
    case 0:
      this.emailInput = obj.emailInput;
      break;
    case 1:
      this.textInput = obj.textInput;
      break;
    case 2:
      this.tzOffset = obj.tzOffset;
      break;
    case 3:
      this.user = obj.user;
      break;
    case 4:
      this.userId = obj.userId;
      break;
    default:
      throw new IllegalArgumentException("out of field index :" + index);
    }
  }

  public void jdoCopyFields(Object obj, int[] indices)
  {
    if (this.jdoStateManager == null)
      throw new IllegalStateException("state manager is null");
    if (indices == null)
      throw new IllegalStateException("fieldNumbers is null");
    if (!(obj instanceof UserPrefs))
      throw new IllegalArgumentException("object is not an object of type com.campusreaderwriter.UserPrefs");
    UserPrefs other = (UserPrefs)obj;
    if (this.jdoStateManager != other.jdoStateManager)
      throw new IllegalArgumentException("state managers do not match");
    int i = indices.length - 1;
    if (i >= 0)
      do
      {
        jdoCopyField(other, indices[i]);
        i--;
      }
      while (i >= 0);
  }

  private static final String[] __jdoFieldNamesInit()
  {
    return new String[] { "emailInput", "textInput", "tzOffset", "user", "userId" };
  }

  private static final Class[] __jdoFieldTypesInit()
  {
    return new Class[] { ___jdo$loadClass("java.lang.String"), ___jdo$loadClass("java.lang.String"), Integer.TYPE, ___jdo$loadClass("com.google.appengine.api.users.User"), ___jdo$loadClass("java.lang.String") };
  }

  private static final byte[] __jdoFieldFlagsInit()
  {
    return new byte[] { 21, 21, 21, 21, 24 };
  }

  protected static int __jdoGetInheritedFieldCount()
  {
    return 0;
  }

  protected static int jdoGetManagedFieldCount()
  {
    return 5;
  }

  private static Class __jdoPersistenceCapableSuperclassInit()
  {
    return null;
  }

  public static Class ___jdo$loadClass(String className)
  {
    try
    {
      return Class.forName(className);
    }
    catch (ClassNotFoundException e)
    {
      throw new NoClassDefFoundError(e.getMessage());
    }
  }

  private Object jdoSuperClone()
    throws CloneNotSupportedException
  {
    UserPrefs o = (UserPrefs)super.clone();
    o.jdoFlags = 0;
    o.jdoStateManager = null;
    return o;
  }

  private void writeObject(ObjectOutputStream out)
    throws IOException
  {
    jdoPreSerialize();
    out.defaultWriteObject();
  }

  private static String jdoGetemailInput(UserPrefs objPC)
  {
    if ((objPC.jdoFlags > 0) && (objPC.jdoStateManager != null) && (!objPC.jdoStateManager.isLoaded(objPC, 0)))
      return objPC.jdoStateManager.getStringField(objPC, 0, objPC.emailInput);
    if ((objPC.jdoIsDetached()) && (!((BitSet)objPC.jdoDetachedState[2]).get(0)))
      throw new JDODetachedFieldAccessException("You have just attempted to access field \"emailInput\" yet this field was not detached when you detached the object. Either dont access this field, or detach it when detaching the object.");
    return objPC.emailInput;
  }

  private static void jdoSetemailInput(UserPrefs objPC, String val)
  {
    if ((objPC.jdoFlags != 0) && (objPC.jdoStateManager != null))
    {
      objPC.jdoStateManager.setStringField(objPC, 0, objPC.emailInput, val);
    }
    else
    {
      objPC.emailInput = val;
      if (objPC.jdoIsDetached())
        ((BitSet)objPC.jdoDetachedState[3]).set(0);
    }
  }

  private static String jdoGettextInput(UserPrefs objPC)
  {
    if ((objPC.jdoFlags > 0) && (objPC.jdoStateManager != null) && (!objPC.jdoStateManager.isLoaded(objPC, 1)))
      return objPC.jdoStateManager.getStringField(objPC, 1, objPC.textInput);
    if ((objPC.jdoIsDetached()) && (!((BitSet)objPC.jdoDetachedState[2]).get(1)))
      throw new JDODetachedFieldAccessException("You have just attempted to access field \"textInput\" yet this field was not detached when you detached the object. Either dont access this field, or detach it when detaching the object.");
    return objPC.textInput;
  }

  private static void jdoSettextInput(UserPrefs objPC, String val)
  {
    if ((objPC.jdoFlags != 0) && (objPC.jdoStateManager != null))
    {
      objPC.jdoStateManager.setStringField(objPC, 1, objPC.textInput, val);
    }
    else
    {
      objPC.textInput = val;
      if (objPC.jdoIsDetached())
        ((BitSet)objPC.jdoDetachedState[3]).set(1);
    }
  }

  private static int jdoGettzOffset(UserPrefs objPC)
  {
    if ((objPC.jdoFlags > 0) && (objPC.jdoStateManager != null) && (!objPC.jdoStateManager.isLoaded(objPC, 2)))
      return objPC.jdoStateManager.getIntField(objPC, 2, objPC.tzOffset);
    if ((objPC.jdoIsDetached()) && (!((BitSet)objPC.jdoDetachedState[2]).get(2)))
      throw new JDODetachedFieldAccessException("You have just attempted to access field \"tzOffset\" yet this field was not detached when you detached the object. Either dont access this field, or detach it when detaching the object.");
    return objPC.tzOffset;
  }

  private static void jdoSettzOffset(UserPrefs objPC, int val)
  {
    if ((objPC.jdoFlags != 0) && (objPC.jdoStateManager != null))
    {
      objPC.jdoStateManager.setIntField(objPC, 2, objPC.tzOffset, val);
    }
    else
    {
      objPC.tzOffset = val;
      if (objPC.jdoIsDetached())
        ((BitSet)objPC.jdoDetachedState[3]).set(2);
    }
  }

  private static User jdoGetuser(UserPrefs objPC)
  {
    if ((objPC.jdoFlags > 0) && (objPC.jdoStateManager != null) && (!objPC.jdoStateManager.isLoaded(objPC, 3)))
      return (User)objPC.jdoStateManager.getObjectField(objPC, 3, objPC.user);
    if ((objPC.jdoIsDetached()) && (!((BitSet)objPC.jdoDetachedState[2]).get(3)))
      throw new JDODetachedFieldAccessException("You have just attempted to access field \"user\" yet this field was not detached when you detached the object. Either dont access this field, or detach it when detaching the object.");
    return objPC.user;
  }

  private static void jdoSetuser(UserPrefs objPC, User val)
  {
    if ((objPC.jdoFlags != 0) && (objPC.jdoStateManager != null))
    {
      objPC.jdoStateManager.setObjectField(objPC, 3, objPC.user, val);
    }
    else
    {
      objPC.user = val;
      if (objPC.jdoIsDetached())
        ((BitSet)objPC.jdoDetachedState[3]).set(3);
    }
  }

  private static String jdoGetuserId(UserPrefs objPC)
  {
    return objPC.userId;
  }

  private static void jdoSetuserId(UserPrefs objPC, String val)
  {
    if (objPC.jdoStateManager == null)
      objPC.userId = val;
    else
      objPC.jdoStateManager.setStringField(objPC, 4, objPC.userId, val);
    if (objPC.jdoIsDetached())
      ((BitSet)objPC.jdoDetachedState[3]).set(4);
  }
}