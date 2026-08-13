package fr.istic.grpc.sommestream;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.64.0)",
    comments = "Source: sommestream.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class SommeStreamServiceGrpc {

  private SommeStreamServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "sommestream.SommeStreamService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<fr.istic.grpc.sommestream.Sommestream.IntMessage,
      fr.istic.grpc.sommestream.Sommestream.SommeMessage> getSommeStreamMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SommeStream",
      requestType = fr.istic.grpc.sommestream.Sommestream.IntMessage.class,
      responseType = fr.istic.grpc.sommestream.Sommestream.SommeMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<fr.istic.grpc.sommestream.Sommestream.IntMessage,
      fr.istic.grpc.sommestream.Sommestream.SommeMessage> getSommeStreamMethod() {
    io.grpc.MethodDescriptor<fr.istic.grpc.sommestream.Sommestream.IntMessage, fr.istic.grpc.sommestream.Sommestream.SommeMessage> getSommeStreamMethod;
    if ((getSommeStreamMethod = SommeStreamServiceGrpc.getSommeStreamMethod) == null) {
      synchronized (SommeStreamServiceGrpc.class) {
        if ((getSommeStreamMethod = SommeStreamServiceGrpc.getSommeStreamMethod) == null) {
          SommeStreamServiceGrpc.getSommeStreamMethod = getSommeStreamMethod =
              io.grpc.MethodDescriptor.<fr.istic.grpc.sommestream.Sommestream.IntMessage, fr.istic.grpc.sommestream.Sommestream.SommeMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SommeStream"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  fr.istic.grpc.sommestream.Sommestream.IntMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  fr.istic.grpc.sommestream.Sommestream.SommeMessage.getDefaultInstance()))
              .setSchemaDescriptor(new SommeStreamServiceMethodDescriptorSupplier("SommeStream"))
              .build();
        }
      }
    }
    return getSommeStreamMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SommeStreamServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SommeStreamServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SommeStreamServiceStub>() {
        @java.lang.Override
        public SommeStreamServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SommeStreamServiceStub(channel, callOptions);
        }
      };
    return SommeStreamServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SommeStreamServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SommeStreamServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SommeStreamServiceBlockingStub>() {
        @java.lang.Override
        public SommeStreamServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SommeStreamServiceBlockingStub(channel, callOptions);
        }
      };
    return SommeStreamServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SommeStreamServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SommeStreamServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SommeStreamServiceFutureStub>() {
        @java.lang.Override
        public SommeStreamServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SommeStreamServiceFutureStub(channel, callOptions);
        }
      };
    return SommeStreamServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default io.grpc.stub.StreamObserver<fr.istic.grpc.sommestream.Sommestream.IntMessage> sommeStream(
        io.grpc.stub.StreamObserver<fr.istic.grpc.sommestream.Sommestream.SommeMessage> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getSommeStreamMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service SommeStreamService.
   */
  public static abstract class SommeStreamServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return SommeStreamServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service SommeStreamService.
   */
  public static final class SommeStreamServiceStub
      extends io.grpc.stub.AbstractAsyncStub<SommeStreamServiceStub> {
    private SommeStreamServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SommeStreamServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SommeStreamServiceStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<fr.istic.grpc.sommestream.Sommestream.IntMessage> sommeStream(
        io.grpc.stub.StreamObserver<fr.istic.grpc.sommestream.Sommestream.SommeMessage> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getSommeStreamMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service SommeStreamService.
   */
  public static final class SommeStreamServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<SommeStreamServiceBlockingStub> {
    private SommeStreamServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SommeStreamServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SommeStreamServiceBlockingStub(channel, callOptions);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service SommeStreamService.
   */
  public static final class SommeStreamServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<SommeStreamServiceFutureStub> {
    private SommeStreamServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SommeStreamServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SommeStreamServiceFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_SOMME_STREAM = 0;

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
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SOMME_STREAM:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.sommeStream(
              (io.grpc.stub.StreamObserver<fr.istic.grpc.sommestream.Sommestream.SommeMessage>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getSommeStreamMethod(),
          io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
            new MethodHandlers<
              fr.istic.grpc.sommestream.Sommestream.IntMessage,
              fr.istic.grpc.sommestream.Sommestream.SommeMessage>(
                service, METHODID_SOMME_STREAM)))
        .build();
  }

  private static abstract class SommeStreamServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SommeStreamServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return fr.istic.grpc.sommestream.Sommestream.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("SommeStreamService");
    }
  }

  private static final class SommeStreamServiceFileDescriptorSupplier
      extends SommeStreamServiceBaseDescriptorSupplier {
    SommeStreamServiceFileDescriptorSupplier() {}
  }

  private static final class SommeStreamServiceMethodDescriptorSupplier
      extends SommeStreamServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    SommeStreamServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (SommeStreamServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SommeStreamServiceFileDescriptorSupplier())
              .addMethod(getSommeStreamMethod())
              .build();
        }
      }
    }
    return result;
  }
}
