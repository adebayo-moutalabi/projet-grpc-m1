package fr.istic.grpc.datenotif;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.64.0)",
    comments = "Source: datenotif.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class DateNotifGrpc {

  private DateNotifGrpc() {}

  public static final java.lang.String SERVICE_NAME = "datenotif.DateNotif";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<fr.istic.grpc.datenotif.Datenotif.DateRequest,
      fr.istic.grpc.datenotif.Datenotif.DateMessage> getSubscribeDateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SubscribeDate",
      requestType = fr.istic.grpc.datenotif.Datenotif.DateRequest.class,
      responseType = fr.istic.grpc.datenotif.Datenotif.DateMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<fr.istic.grpc.datenotif.Datenotif.DateRequest,
      fr.istic.grpc.datenotif.Datenotif.DateMessage> getSubscribeDateMethod() {
    io.grpc.MethodDescriptor<fr.istic.grpc.datenotif.Datenotif.DateRequest, fr.istic.grpc.datenotif.Datenotif.DateMessage> getSubscribeDateMethod;
    if ((getSubscribeDateMethod = DateNotifGrpc.getSubscribeDateMethod) == null) {
      synchronized (DateNotifGrpc.class) {
        if ((getSubscribeDateMethod = DateNotifGrpc.getSubscribeDateMethod) == null) {
          DateNotifGrpc.getSubscribeDateMethod = getSubscribeDateMethod =
              io.grpc.MethodDescriptor.<fr.istic.grpc.datenotif.Datenotif.DateRequest, fr.istic.grpc.datenotif.Datenotif.DateMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SubscribeDate"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  fr.istic.grpc.datenotif.Datenotif.DateRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  fr.istic.grpc.datenotif.Datenotif.DateMessage.getDefaultInstance()))
              .setSchemaDescriptor(new DateNotifMethodDescriptorSupplier("SubscribeDate"))
              .build();
        }
      }
    }
    return getSubscribeDateMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DateNotifStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DateNotifStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DateNotifStub>() {
        @java.lang.Override
        public DateNotifStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DateNotifStub(channel, callOptions);
        }
      };
    return DateNotifStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DateNotifBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DateNotifBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DateNotifBlockingStub>() {
        @java.lang.Override
        public DateNotifBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DateNotifBlockingStub(channel, callOptions);
        }
      };
    return DateNotifBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DateNotifFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DateNotifFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DateNotifFutureStub>() {
        @java.lang.Override
        public DateNotifFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DateNotifFutureStub(channel, callOptions);
        }
      };
    return DateNotifFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void subscribeDate(fr.istic.grpc.datenotif.Datenotif.DateRequest request,
        io.grpc.stub.StreamObserver<fr.istic.grpc.datenotif.Datenotif.DateMessage> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSubscribeDateMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service DateNotif.
   */
  public static abstract class DateNotifImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return DateNotifGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service DateNotif.
   */
  public static final class DateNotifStub
      extends io.grpc.stub.AbstractAsyncStub<DateNotifStub> {
    private DateNotifStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DateNotifStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DateNotifStub(channel, callOptions);
    }

    /**
     */
    public void subscribeDate(fr.istic.grpc.datenotif.Datenotif.DateRequest request,
        io.grpc.stub.StreamObserver<fr.istic.grpc.datenotif.Datenotif.DateMessage> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getSubscribeDateMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service DateNotif.
   */
  public static final class DateNotifBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<DateNotifBlockingStub> {
    private DateNotifBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DateNotifBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DateNotifBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<fr.istic.grpc.datenotif.Datenotif.DateMessage> subscribeDate(
        fr.istic.grpc.datenotif.Datenotif.DateRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getSubscribeDateMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service DateNotif.
   */
  public static final class DateNotifFutureStub
      extends io.grpc.stub.AbstractFutureStub<DateNotifFutureStub> {
    private DateNotifFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DateNotifFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DateNotifFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_SUBSCRIBE_DATE = 0;

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
        case METHODID_SUBSCRIBE_DATE:
          serviceImpl.subscribeDate((fr.istic.grpc.datenotif.Datenotif.DateRequest) request,
              (io.grpc.stub.StreamObserver<fr.istic.grpc.datenotif.Datenotif.DateMessage>) responseObserver);
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
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getSubscribeDateMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              fr.istic.grpc.datenotif.Datenotif.DateRequest,
              fr.istic.grpc.datenotif.Datenotif.DateMessage>(
                service, METHODID_SUBSCRIBE_DATE)))
        .build();
  }

  private static abstract class DateNotifBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DateNotifBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return fr.istic.grpc.datenotif.Datenotif.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DateNotif");
    }
  }

  private static final class DateNotifFileDescriptorSupplier
      extends DateNotifBaseDescriptorSupplier {
    DateNotifFileDescriptorSupplier() {}
  }

  private static final class DateNotifMethodDescriptorSupplier
      extends DateNotifBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    DateNotifMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (DateNotifGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DateNotifFileDescriptorSupplier())
              .addMethod(getSubscribeDateMethod())
              .build();
        }
      }
    }
    return result;
  }
}
