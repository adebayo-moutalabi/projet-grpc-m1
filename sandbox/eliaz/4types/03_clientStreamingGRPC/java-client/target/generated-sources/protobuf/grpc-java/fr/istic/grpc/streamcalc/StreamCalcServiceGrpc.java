package fr.istic.grpc.streamcalc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.64.0)",
    comments = "Source: streamcalc.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class StreamCalcServiceGrpc {

  private StreamCalcServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "streamcalc.StreamCalcService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<fr.istic.grpc.streamcalc.Streamcalc.NumberRequest,
      fr.istic.grpc.streamcalc.Streamcalc.SumReply> getSumMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Sum",
      requestType = fr.istic.grpc.streamcalc.Streamcalc.NumberRequest.class,
      responseType = fr.istic.grpc.streamcalc.Streamcalc.SumReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<fr.istic.grpc.streamcalc.Streamcalc.NumberRequest,
      fr.istic.grpc.streamcalc.Streamcalc.SumReply> getSumMethod() {
    io.grpc.MethodDescriptor<fr.istic.grpc.streamcalc.Streamcalc.NumberRequest, fr.istic.grpc.streamcalc.Streamcalc.SumReply> getSumMethod;
    if ((getSumMethod = StreamCalcServiceGrpc.getSumMethod) == null) {
      synchronized (StreamCalcServiceGrpc.class) {
        if ((getSumMethod = StreamCalcServiceGrpc.getSumMethod) == null) {
          StreamCalcServiceGrpc.getSumMethod = getSumMethod =
              io.grpc.MethodDescriptor.<fr.istic.grpc.streamcalc.Streamcalc.NumberRequest, fr.istic.grpc.streamcalc.Streamcalc.SumReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Sum"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  fr.istic.grpc.streamcalc.Streamcalc.NumberRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  fr.istic.grpc.streamcalc.Streamcalc.SumReply.getDefaultInstance()))
              .setSchemaDescriptor(new StreamCalcServiceMethodDescriptorSupplier("Sum"))
              .build();
        }
      }
    }
    return getSumMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static StreamCalcServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StreamCalcServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StreamCalcServiceStub>() {
        @java.lang.Override
        public StreamCalcServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StreamCalcServiceStub(channel, callOptions);
        }
      };
    return StreamCalcServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static StreamCalcServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StreamCalcServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StreamCalcServiceBlockingStub>() {
        @java.lang.Override
        public StreamCalcServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StreamCalcServiceBlockingStub(channel, callOptions);
        }
      };
    return StreamCalcServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static StreamCalcServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StreamCalcServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StreamCalcServiceFutureStub>() {
        @java.lang.Override
        public StreamCalcServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StreamCalcServiceFutureStub(channel, callOptions);
        }
      };
    return StreamCalcServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default io.grpc.stub.StreamObserver<fr.istic.grpc.streamcalc.Streamcalc.NumberRequest> sum(
        io.grpc.stub.StreamObserver<fr.istic.grpc.streamcalc.Streamcalc.SumReply> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getSumMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service StreamCalcService.
   */
  public static abstract class StreamCalcServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return StreamCalcServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service StreamCalcService.
   */
  public static final class StreamCalcServiceStub
      extends io.grpc.stub.AbstractAsyncStub<StreamCalcServiceStub> {
    private StreamCalcServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StreamCalcServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StreamCalcServiceStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<fr.istic.grpc.streamcalc.Streamcalc.NumberRequest> sum(
        io.grpc.stub.StreamObserver<fr.istic.grpc.streamcalc.Streamcalc.SumReply> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncClientStreamingCall(
          getChannel().newCall(getSumMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service StreamCalcService.
   */
  public static final class StreamCalcServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<StreamCalcServiceBlockingStub> {
    private StreamCalcServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StreamCalcServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StreamCalcServiceBlockingStub(channel, callOptions);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service StreamCalcService.
   */
  public static final class StreamCalcServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<StreamCalcServiceFutureStub> {
    private StreamCalcServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StreamCalcServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StreamCalcServiceFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_SUM = 0;

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
        case METHODID_SUM:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.sum(
              (io.grpc.stub.StreamObserver<fr.istic.grpc.streamcalc.Streamcalc.SumReply>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getSumMethod(),
          io.grpc.stub.ServerCalls.asyncClientStreamingCall(
            new MethodHandlers<
              fr.istic.grpc.streamcalc.Streamcalc.NumberRequest,
              fr.istic.grpc.streamcalc.Streamcalc.SumReply>(
                service, METHODID_SUM)))
        .build();
  }

  private static abstract class StreamCalcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    StreamCalcServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return fr.istic.grpc.streamcalc.Streamcalc.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("StreamCalcService");
    }
  }

  private static final class StreamCalcServiceFileDescriptorSupplier
      extends StreamCalcServiceBaseDescriptorSupplier {
    StreamCalcServiceFileDescriptorSupplier() {}
  }

  private static final class StreamCalcServiceMethodDescriptorSupplier
      extends StreamCalcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    StreamCalcServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (StreamCalcServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new StreamCalcServiceFileDescriptorSupplier())
              .addMethod(getSumMethod())
              .build();
        }
      }
    }
    return result;
  }
}
