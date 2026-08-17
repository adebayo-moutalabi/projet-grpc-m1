package fr.istic.grpc.delivery;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.64.0)",
    comments = "Source: delivery.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class DeliveryServiceGrpc {

  private DeliveryServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "delivery.DeliveryService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<fr.istic.grpc.delivery.Delivery.OrderRequest,
      fr.istic.grpc.delivery.Delivery.OrderResponse> getCreateOrderMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateOrder",
      requestType = fr.istic.grpc.delivery.Delivery.OrderRequest.class,
      responseType = fr.istic.grpc.delivery.Delivery.OrderResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<fr.istic.grpc.delivery.Delivery.OrderRequest,
      fr.istic.grpc.delivery.Delivery.OrderResponse> getCreateOrderMethod() {
    io.grpc.MethodDescriptor<fr.istic.grpc.delivery.Delivery.OrderRequest, fr.istic.grpc.delivery.Delivery.OrderResponse> getCreateOrderMethod;
    if ((getCreateOrderMethod = DeliveryServiceGrpc.getCreateOrderMethod) == null) {
      synchronized (DeliveryServiceGrpc.class) {
        if ((getCreateOrderMethod = DeliveryServiceGrpc.getCreateOrderMethod) == null) {
          DeliveryServiceGrpc.getCreateOrderMethod = getCreateOrderMethod =
              io.grpc.MethodDescriptor.<fr.istic.grpc.delivery.Delivery.OrderRequest, fr.istic.grpc.delivery.Delivery.OrderResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateOrder"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  fr.istic.grpc.delivery.Delivery.OrderRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  fr.istic.grpc.delivery.Delivery.OrderResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DeliveryServiceMethodDescriptorSupplier("CreateOrder"))
              .build();
        }
      }
    }
    return getCreateOrderMethod;
  }

  private static volatile io.grpc.MethodDescriptor<fr.istic.grpc.delivery.Delivery.TrackRequest,
      fr.istic.grpc.delivery.Delivery.Position> getTrackDeliveryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "TrackDelivery",
      requestType = fr.istic.grpc.delivery.Delivery.TrackRequest.class,
      responseType = fr.istic.grpc.delivery.Delivery.Position.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<fr.istic.grpc.delivery.Delivery.TrackRequest,
      fr.istic.grpc.delivery.Delivery.Position> getTrackDeliveryMethod() {
    io.grpc.MethodDescriptor<fr.istic.grpc.delivery.Delivery.TrackRequest, fr.istic.grpc.delivery.Delivery.Position> getTrackDeliveryMethod;
    if ((getTrackDeliveryMethod = DeliveryServiceGrpc.getTrackDeliveryMethod) == null) {
      synchronized (DeliveryServiceGrpc.class) {
        if ((getTrackDeliveryMethod = DeliveryServiceGrpc.getTrackDeliveryMethod) == null) {
          DeliveryServiceGrpc.getTrackDeliveryMethod = getTrackDeliveryMethod =
              io.grpc.MethodDescriptor.<fr.istic.grpc.delivery.Delivery.TrackRequest, fr.istic.grpc.delivery.Delivery.Position>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "TrackDelivery"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  fr.istic.grpc.delivery.Delivery.TrackRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  fr.istic.grpc.delivery.Delivery.Position.getDefaultInstance()))
              .setSchemaDescriptor(new DeliveryServiceMethodDescriptorSupplier("TrackDelivery"))
              .build();
        }
      }
    }
    return getTrackDeliveryMethod;
  }

  private static volatile io.grpc.MethodDescriptor<fr.istic.grpc.delivery.Delivery.Photo,
      fr.istic.grpc.delivery.Delivery.UploadAck> getUploadProofMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UploadProof",
      requestType = fr.istic.grpc.delivery.Delivery.Photo.class,
      responseType = fr.istic.grpc.delivery.Delivery.UploadAck.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<fr.istic.grpc.delivery.Delivery.Photo,
      fr.istic.grpc.delivery.Delivery.UploadAck> getUploadProofMethod() {
    io.grpc.MethodDescriptor<fr.istic.grpc.delivery.Delivery.Photo, fr.istic.grpc.delivery.Delivery.UploadAck> getUploadProofMethod;
    if ((getUploadProofMethod = DeliveryServiceGrpc.getUploadProofMethod) == null) {
      synchronized (DeliveryServiceGrpc.class) {
        if ((getUploadProofMethod = DeliveryServiceGrpc.getUploadProofMethod) == null) {
          DeliveryServiceGrpc.getUploadProofMethod = getUploadProofMethod =
              io.grpc.MethodDescriptor.<fr.istic.grpc.delivery.Delivery.Photo, fr.istic.grpc.delivery.Delivery.UploadAck>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UploadProof"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  fr.istic.grpc.delivery.Delivery.Photo.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  fr.istic.grpc.delivery.Delivery.UploadAck.getDefaultInstance()))
              .setSchemaDescriptor(new DeliveryServiceMethodDescriptorSupplier("UploadProof"))
              .build();
        }
      }
    }
    return getUploadProofMethod;
  }

  private static volatile io.grpc.MethodDescriptor<fr.istic.grpc.delivery.Delivery.ChatMessage,
      fr.istic.grpc.delivery.Delivery.ChatMessage> getSupportChatMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SupportChat",
      requestType = fr.istic.grpc.delivery.Delivery.ChatMessage.class,
      responseType = fr.istic.grpc.delivery.Delivery.ChatMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<fr.istic.grpc.delivery.Delivery.ChatMessage,
      fr.istic.grpc.delivery.Delivery.ChatMessage> getSupportChatMethod() {
    io.grpc.MethodDescriptor<fr.istic.grpc.delivery.Delivery.ChatMessage, fr.istic.grpc.delivery.Delivery.ChatMessage> getSupportChatMethod;
    if ((getSupportChatMethod = DeliveryServiceGrpc.getSupportChatMethod) == null) {
      synchronized (DeliveryServiceGrpc.class) {
        if ((getSupportChatMethod = DeliveryServiceGrpc.getSupportChatMethod) == null) {
          DeliveryServiceGrpc.getSupportChatMethod = getSupportChatMethod =
              io.grpc.MethodDescriptor.<fr.istic.grpc.delivery.Delivery.ChatMessage, fr.istic.grpc.delivery.Delivery.ChatMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SupportChat"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  fr.istic.grpc.delivery.Delivery.ChatMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  fr.istic.grpc.delivery.Delivery.ChatMessage.getDefaultInstance()))
              .setSchemaDescriptor(new DeliveryServiceMethodDescriptorSupplier("SupportChat"))
              .build();
        }
      }
    }
    return getSupportChatMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DeliveryServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DeliveryServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DeliveryServiceStub>() {
        @java.lang.Override
        public DeliveryServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DeliveryServiceStub(channel, callOptions);
        }
      };
    return DeliveryServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DeliveryServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DeliveryServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DeliveryServiceBlockingStub>() {
        @java.lang.Override
        public DeliveryServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DeliveryServiceBlockingStub(channel, callOptions);
        }
      };
    return DeliveryServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DeliveryServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DeliveryServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DeliveryServiceFutureStub>() {
        @java.lang.Override
        public DeliveryServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DeliveryServiceFutureStub(channel, callOptions);
        }
      };
    return DeliveryServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     * <pre>
     * 1 UNARY : création d'une commande par le livreur, le serveur lui renvoie confirmation + ID généré
     * </pre>
     */
    default void createOrder(fr.istic.grpc.delivery.Delivery.OrderRequest request,
        io.grpc.stub.StreamObserver<fr.istic.grpc.delivery.Delivery.OrderResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateOrderMethod(), responseObserver);
    }

    /**
     * <pre>
     * 2 SSTREAMING : le client s'abonne et reçoit la position GPS du livreur toutes les 10s
     * </pre>
     */
    default void trackDelivery(fr.istic.grpc.delivery.Delivery.TrackRequest request,
        io.grpc.stub.StreamObserver<fr.istic.grpc.delivery.Delivery.Position> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getTrackDeliveryMethod(), responseObserver);
    }

    /**
     * <pre>
     * 3 CSTREAMING : le livreur envoie plusieurs photos, le serveur ACK une fois toutes reçues
     * </pre>
     */
    default io.grpc.stub.StreamObserver<fr.istic.grpc.delivery.Delivery.Photo> uploadProof(
        io.grpc.stub.StreamObserver<fr.istic.grpc.delivery.Delivery.UploadAck> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getUploadProofMethod(), responseObserver);
    }

    /**
     * <pre>
     * 4 BIDISTREAMING : chat client/livreur pour en cas de problème
     * </pre>
     */
    default io.grpc.stub.StreamObserver<fr.istic.grpc.delivery.Delivery.ChatMessage> supportChat(
        io.grpc.stub.StreamObserver<fr.istic.grpc.delivery.Delivery.ChatMessage> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getSupportChatMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service DeliveryService.
   */
  public static abstract class DeliveryServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return DeliveryServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service DeliveryService.
   */
  public static final class DeliveryServiceStub
      extends io.grpc.stub.AbstractAsyncStub<DeliveryServiceStub> {
    private DeliveryServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DeliveryServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DeliveryServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * 1 UNARY : création d'une commande par le livreur, le serveur lui renvoie confirmation + ID généré
     * </pre>
     */
    public void createOrder(fr.istic.grpc.delivery.Delivery.OrderRequest request,
        io.grpc.stub.StreamObserver<fr.istic.grpc.delivery.Delivery.OrderResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateOrderMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 2 SSTREAMING : le client s'abonne et reçoit la position GPS du livreur toutes les 10s
     * </pre>
     */
    public void trackDelivery(fr.istic.grpc.delivery.Delivery.TrackRequest request,
        io.grpc.stub.StreamObserver<fr.istic.grpc.delivery.Delivery.Position> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getTrackDeliveryMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 3 CSTREAMING : le livreur envoie plusieurs photos, le serveur ACK une fois toutes reçues
     * </pre>
     */
    public io.grpc.stub.StreamObserver<fr.istic.grpc.delivery.Delivery.Photo> uploadProof(
        io.grpc.stub.StreamObserver<fr.istic.grpc.delivery.Delivery.UploadAck> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncClientStreamingCall(
          getChannel().newCall(getUploadProofMethod(), getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     * 4 BIDISTREAMING : chat client/livreur pour en cas de problème
     * </pre>
     */
    public io.grpc.stub.StreamObserver<fr.istic.grpc.delivery.Delivery.ChatMessage> supportChat(
        io.grpc.stub.StreamObserver<fr.istic.grpc.delivery.Delivery.ChatMessage> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getSupportChatMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service DeliveryService.
   */
  public static final class DeliveryServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<DeliveryServiceBlockingStub> {
    private DeliveryServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DeliveryServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DeliveryServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * 1 UNARY : création d'une commande par le livreur, le serveur lui renvoie confirmation + ID généré
     * </pre>
     */
    public fr.istic.grpc.delivery.Delivery.OrderResponse createOrder(fr.istic.grpc.delivery.Delivery.OrderRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateOrderMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 2 SSTREAMING : le client s'abonne et reçoit la position GPS du livreur toutes les 10s
     * </pre>
     */
    public java.util.Iterator<fr.istic.grpc.delivery.Delivery.Position> trackDelivery(
        fr.istic.grpc.delivery.Delivery.TrackRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getTrackDeliveryMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service DeliveryService.
   */
  public static final class DeliveryServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<DeliveryServiceFutureStub> {
    private DeliveryServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DeliveryServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DeliveryServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * 1 UNARY : création d'une commande par le livreur, le serveur lui renvoie confirmation + ID généré
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<fr.istic.grpc.delivery.Delivery.OrderResponse> createOrder(
        fr.istic.grpc.delivery.Delivery.OrderRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateOrderMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_ORDER = 0;
  private static final int METHODID_TRACK_DELIVERY = 1;
  private static final int METHODID_UPLOAD_PROOF = 2;
  private static final int METHODID_SUPPORT_CHAT = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CREATE_ORDER:
          serviceImpl.createOrder((fr.istic.grpc.delivery.Delivery.OrderRequest) request,
              (io.grpc.stub.StreamObserver<fr.istic.grpc.delivery.Delivery.OrderResponse>) responseObserver);
          break;
        case METHODID_TRACK_DELIVERY:
          serviceImpl.trackDelivery((fr.istic.grpc.delivery.Delivery.TrackRequest) request,
              (io.grpc.stub.StreamObserver<fr.istic.grpc.delivery.Delivery.Position>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_UPLOAD_PROOF:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.uploadProof(
              (io.grpc.stub.StreamObserver<fr.istic.grpc.delivery.Delivery.UploadAck>) responseObserver);
        case METHODID_SUPPORT_CHAT:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.supportChat(
              (io.grpc.stub.StreamObserver<fr.istic.grpc.delivery.Delivery.ChatMessage>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getCreateOrderMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              fr.istic.grpc.delivery.Delivery.OrderRequest,
              fr.istic.grpc.delivery.Delivery.OrderResponse>(
                service, METHODID_CREATE_ORDER)))
        .addMethod(
          getTrackDeliveryMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              fr.istic.grpc.delivery.Delivery.TrackRequest,
              fr.istic.grpc.delivery.Delivery.Position>(
                service, METHODID_TRACK_DELIVERY)))
        .addMethod(
          getUploadProofMethod(),
          io.grpc.stub.ServerCalls.asyncClientStreamingCall(
            new MethodHandlers<
              fr.istic.grpc.delivery.Delivery.Photo,
              fr.istic.grpc.delivery.Delivery.UploadAck>(
                service, METHODID_UPLOAD_PROOF)))
        .addMethod(
          getSupportChatMethod(),
          io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
            new MethodHandlers<
              fr.istic.grpc.delivery.Delivery.ChatMessage,
              fr.istic.grpc.delivery.Delivery.ChatMessage>(
                service, METHODID_SUPPORT_CHAT)))
        .build();
  }

  private static abstract class DeliveryServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DeliveryServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return fr.istic.grpc.delivery.Delivery.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DeliveryService");
    }
  }

  private static final class DeliveryServiceFileDescriptorSupplier
      extends DeliveryServiceBaseDescriptorSupplier {
    DeliveryServiceFileDescriptorSupplier() {}
  }

  private static final class DeliveryServiceMethodDescriptorSupplier
      extends DeliveryServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    DeliveryServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (DeliveryServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DeliveryServiceFileDescriptorSupplier())
              .addMethod(getCreateOrderMethod())
              .addMethod(getTrackDeliveryMethod())
              .addMethod(getUploadProofMethod())
              .addMethod(getSupportChatMethod())
              .build();
        }
      }
    }
    return result;
  }
}
