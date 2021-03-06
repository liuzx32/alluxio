// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: grpc/common.proto

package alluxio.grpc;

/**
 * Protobuf enum {@code alluxio.grpc.TtlAction}
 */
public enum TtlAction
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <pre>
   * Delete the file after TTL expires.
   * </pre>
   *
   * <code>DELETE = 0;</code>
   */
  DELETE(0),
  /**
   * <pre>
   * Free the file after TTL expires.
   * </pre>
   *
   * <code>FREE = 1;</code>
   */
  FREE(1),
  ;

  /**
   * <pre>
   * Delete the file after TTL expires.
   * </pre>
   *
   * <code>DELETE = 0;</code>
   */
  public static final int DELETE_VALUE = 0;
  /**
   * <pre>
   * Free the file after TTL expires.
   * </pre>
   *
   * <code>FREE = 1;</code>
   */
  public static final int FREE_VALUE = 1;


  public final int getNumber() {
    return value;
  }

  /**
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static TtlAction valueOf(int value) {
    return forNumber(value);
  }

  public static TtlAction forNumber(int value) {
    switch (value) {
      case 0: return DELETE;
      case 1: return FREE;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<TtlAction>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      TtlAction> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<TtlAction>() {
          public TtlAction findValueByNumber(int number) {
            return TtlAction.forNumber(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    return getDescriptor().getValues().get(ordinal());
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return alluxio.grpc.CommonProto.getDescriptor().getEnumTypes().get(3);
  }

  private static final TtlAction[] VALUES = values();

  public static TtlAction valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private TtlAction(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:alluxio.grpc.TtlAction)
}

